package com.lzf.mall.service.impl;

import com.lzf.mall.common.Constant;
import com.lzf.mall.exception.MallException;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.model.dao.CartMapper;
import com.lzf.mall.model.dao.ProductMapper;
import com.lzf.mall.model.pojo.Cart;
import com.lzf.mall.model.pojo.Product;
import com.lzf.mall.model.vo.CartVO;
import com.lzf.mall.service.CartService;
import com.lzf.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-22-15:29
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;


    @Override
    public List<CartVO> list(Integer userId){
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        //计算总价
        for (CartVO cartVO : cartVOS) {
            int totalPrice=cartVO.getPrice()*cartVO.getQuantity();
            cartVO.setTotalPrice(totalPrice);
        }
        return cartVOS;
    }
    @Override
    public List<CartVO> addCart(Integer userId, Integer productId, Integer count){
        validProduct(productId,count);
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            //购物车没有商品 添加新的商品
            cart=new Cart();
            cart.setQuantity(count);
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setSelected(Constant.Selected.CHECKED);
            cartMapper.insertSelective(cart);
        }else{
            //购物车存在该商品 在原有商品的数量追加
            count=cart.getQuantity()+count;
            Cart newCart=new Cart();
            newCart.setId(cart.getId());
            newCart.setQuantity(count);
            newCart.setProductId(cart.getProductId());
            newCart.setUserId(cart.getUserId());
            newCart.setSelected(Constant.Selected.CHECKED);
            cartMapper.updateByPrimaryKeySelective(newCart);
        }
        return this.list(userId);
    }

    /**
     * 检验商品是否在售，库存是否充足
     * @param productId
     * @param count
     */
    private void validProduct(Integer productId,Integer count){
        Product product = productMapper.selectByPrimaryKey(productId);
        //判断是否有商品还有商品是否上架
        if (product ==null || product.getStatus().equals(Constant.getProductStatus.NOT_STATUS)){
            throw new MallException(MallExceptionEnum.PRODUCT_FAILED);
        }
        //判断商品库存
        if (count> product.getStock()){
            throw new MallException(MallExceptionEnum.PRODUCT_STOCK);
        }
    }

    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count){
        validProduct(productId,count);
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
           throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }else{
            //购物车存在该商品 修改商品数量
            Cart newCart=new Cart();
            newCart.setId(cart.getId());
            newCart.setQuantity(count);
            newCart.setProductId(cart.getProductId());
            newCart.setUserId(cart.getUserId());
            newCart.setSelected(Constant.Selected.CHECKED);
            cartMapper.updateByPrimaryKeySelective(newCart);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> delete(Integer userId, Integer productId){
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }else{
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected){
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }else{
            cartMapper.selectOrNot(userId, productId, selected);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selected){
        cartMapper.selectOrNot(userId,null,selected);
        return this.list(userId);
    }
}
