package com.lzf.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.common.R;
import com.lzf.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lianzhengfeng
 * @create 2021-01-26-21:00
 */
@RestController
@CrossOrigin
public class OrderAdminController {
    @Autowired
    private OrderService orderService;

    /**
     * 管理员订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/admin/order/list")
    public R listForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return R.success(pageInfo);
    }

    /**
     * 订单发货
     */
    @PostMapping("/admin/order/delivered")
    public R delivered(@RequestParam String orderNo){
        orderService.delivered(orderNo);
        return R.success();
    }

    /**
     * 订单完结
     */
    @PostMapping("/order/finish")
    public R finish(@RequestParam String orderNo){
        orderService.finish(orderNo);
        return R.success();
    }
}
