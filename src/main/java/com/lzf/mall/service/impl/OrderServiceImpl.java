package com.lzf.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.lzf.mall.common.Constant;
import com.lzf.mall.exception.MallException;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.filter.UserFilter;
import com.lzf.mall.model.dao.CartMapper;
import com.lzf.mall.model.dao.OrderItemMapper;
import com.lzf.mall.model.dao.OrderMapper;
import com.lzf.mall.model.dao.ProductMapper;
import com.lzf.mall.model.pojo.Order;
import com.lzf.mall.model.pojo.OrderItem;
import com.lzf.mall.model.pojo.Product;
import com.lzf.mall.model.request.CreateOrderReq;
import com.lzf.mall.model.vo.CartVO;
import com.lzf.mall.model.vo.OrderItemVO;
import com.lzf.mall.model.vo.OrderVO;
import com.lzf.mall.service.CartService;
import com.lzf.mall.service.OrderService;
import com.lzf.mall.service.UserService;
import com.lzf.mall.util.OrderCodeFactory;
import com.lzf.mall.util.QRCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-25-13:45
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    CartService cartService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    UserService userService;

    @Value("${file.upload.ip}")
    String ip;
    //数据库事务 遇到异常回滚事务
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String create(CreateOrderReq createOrderReq){
        //获取用户id
        Integer userId = UserFilter.currentUser.getId();
        //从购物车查找已勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartCheckedTemp = new ArrayList<>();
        for (CartVO cartVO : cartVOList) {
            if (cartVO.getSelected().equals(Constant.Selected.CHECKED)){
                cartCheckedTemp.add(cartVO);
            }
        }
        cartVOList=cartCheckedTemp;
        //如果购物车已勾选的商品为空,抛出异常
        if (CollectionUtils.isEmpty(cartVOList)){
            throw new MallException(MallExceptionEnum.CART_FAILED);
        }
        //判断商品是否存在,商品上下架状态,库存
        vaildProductAndStatusOrStock(cartVOList);
        //把购物车对象转换位订单item对象
        List<OrderItem> orderItemList = cartVoListSwitchOrderItemList(cartVOList);
        //扣库存
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock=product.getStock()-orderItem.getQuantity();
            if (stock < 0){
                throw new MallException(MallExceptionEnum.PRODUCT_STOCK);
            }
            product.setStock(stock);
            //更新库存
            productMapper.updateByPrimaryKeySelective(product);
        }
        //把购物车已勾选的商品删除
        cleanCart(cartVOList);
        //生成订单
        Order order=new Order();
        //生成订单号
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setOrderStatus(Constant.OrderStatusEnum.Not_PAYING.getCode());
        order.setTotalPrice(totalPrice(orderItemList));
        order.setPaymentType(1);
        order.setPostage(0);
        orderMapper.insertSelective(order);
        //循环保存每个商品到order_item表
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        //把结果返回
        return orderNo;
    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        int totalPrice=0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice+=orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    private void cleanCart(List<CartVO> cartVOList) {
        for (CartVO cartVO : cartVOList) {
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    private List<OrderItem> cartVoListSwitchOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList=new ArrayList<>();
        for (CartVO cartVO : cartVOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImages());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void vaildProductAndStatusOrStock(List<CartVO> cartVOList) {
        for (CartVO cartVO : cartVOList) {
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            //判断是否有商品还有商品是否上架
            if (product ==null || product.getStatus().equals(Constant.getProductStatus.NOT_STATUS)){
                throw new MallException(MallExceptionEnum.PRODUCT_FAILED);
            }
            //判断商品库存
            if (cartVO.getQuantity()> product.getStock()){
                throw new MallException(MallExceptionEnum.PRODUCT_STOCK);
            }
        }
    }

    @Override
    public OrderVO detail(String orderNo){
        Order order=orderMapper.selectByOrderNo(orderNo);
        //根据传来的订单号进行判断订单是否有该订单
        if (order == null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //订单存在 判断订单是否属于当前登录的用户
        Integer userId = UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)){
            throw new MallException(MallExceptionEnum.ORDER_NO_YOUR);
        }
        //把order对象转换成OrderVo对象返回
        OrderVO orderVO=getOrderVO(order);
        return orderVO;
    }

    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO=new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        List<OrderItem> orderItemList=orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList=new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVO orderItemVO=new OrderItemVO();
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }


    @Override
    public PageInfo listForCoustomer(Integer pageNum, Integer pageSize){
        Integer userId = UserFilter.currentUser.getId();
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList=orderMapper.selectByUserId(userId);
        //将orderList转换成OrderVOList
        List<OrderVO> orderVOList=orderListToOrderVoList(orderList);
        PageInfo pageInfo=new PageInfo(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    private List<OrderVO> orderListToOrderVoList(List<Order> orderList) {
        ArrayList<OrderVO> orderVOS = new ArrayList<>();
        for (Order order : orderList) {
            OrderVO orderVO = getOrderVO(order);
            orderVOS.add(orderVO);
        }
        return orderVOS;
    }

    @Override
    public void cancel(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        Integer userId = UserFilter.currentUser.getId();
        if (order == null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        if (!order.getUserId().equals(userId)){
            throw new MallException(MallExceptionEnum.ORDER_NO_YOUR);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.Not_PAYING.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.USER_CANCEL.getCode());
            order.setEndTime(new Date());
            //更新修改的状态时间
            orderMapper.updateByPrimaryKeySelective(order);
        }
    }
    @Override
    public String qrcode(String orderNo){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String address = ip + ":" + request.getLocalPort();
        String payUrl = "http://" + address + "/pay?orderNo=" + orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl, 350, 350, Constant.FILE_UPLOAD_DIR + orderNo + ".png");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pngAddress = "http://" + address + "/images/" + orderNo + ".png";
        return pngAddress;
    }

    @Override
    public void pay(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.Not_PAYING.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.PAYING.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MallException(MallExceptionEnum.ORDER_STATUS_ERROR);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList=orderMapper.selectAllAdmin();
        //将orderList转换成OrderVOList
        List<OrderVO> orderVOList=orderListToOrderVoList(orderList);
        PageInfo pageInfo=new PageInfo(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Override
    public void delivered(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.PAYING.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERY.getCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MallException(MallExceptionEnum.ORDER_STATUS_ERROR);
        }
    }

    @Override
    public void finish(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        //如果是普通用户调用 判断该订单是否属于该用户
        if (!userService.checkAdminRole(UserFilter.currentUser) && !order.getUserId().equals(UserFilter.currentUser.getId())){
            throw new MallException(MallExceptionEnum.ORDER_NO_YOUR);
        }
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.DELIVERY.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MallException(MallExceptionEnum.ORDER_STATUS_ERROR);
        }
    }
}
