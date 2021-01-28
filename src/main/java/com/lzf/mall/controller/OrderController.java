package com.lzf.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.common.R;
import com.lzf.mall.model.request.CreateOrderReq;
import com.lzf.mall.model.vo.OrderVO;
import com.lzf.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lianzhengfeng
 * @create 2021-01-25-13:41
 */
@RestController
@CrossOrigin
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/order/create")
    public R create(@RequestBody CreateOrderReq createOrderReq){
        String orderNo = orderService.create(createOrderReq);
        return R.success(orderNo);
    }

    /**
     * 查询订单详情
     * @param orderNo
     * @return
     */
    @GetMapping("/order/detail")
    public R detail(@RequestParam String orderNo){
        OrderVO orderVO = orderService.detail(orderNo);
        return R.success(orderVO);
    }

    @GetMapping("/order/list")
    public R list(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForCoustomer(pageNum, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 取消订单
     */
    @PostMapping("/order/cancel")
    public R cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return R.success();
    }

    /**
     * 生成二维码
     * @param orderNo
     * @return
     */
    @GetMapping("/order/qrcode")
    public R qrcode(@RequestParam String orderNo){
        String pngAddress = orderService.qrcode(orderNo);
        return R.success(pngAddress);
    }

    /**
     * 支付订单
     * @param orderNo
     * @return
     */
    @GetMapping("pay")
    public R pay(@RequestParam String orderNo){
        orderService.pay(orderNo);
        return R.success();
    }

}
