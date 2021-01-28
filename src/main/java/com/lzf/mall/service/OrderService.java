package com.lzf.mall.service;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.model.request.CreateOrderReq;
import com.lzf.mall.model.vo.CartVO;
import com.lzf.mall.model.vo.OrderVO;

import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-15-21:24
 */
public interface OrderService {


    String create(CreateOrderReq createOrderReq);

    OrderVO detail(String orderNo);

    PageInfo listForCoustomer(Integer pageNum, Integer pageSize);

    void cancel(String orderNo);

    String qrcode(String orderNo);

    void pay(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void delivered(String orderNo);

    void finish(String orderNo);
}
