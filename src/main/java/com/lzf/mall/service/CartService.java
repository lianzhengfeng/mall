package com.lzf.mall.service;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.model.pojo.Product;
import com.lzf.mall.model.request.AddProductReq;
import com.lzf.mall.model.request.ProductListReq;
import com.lzf.mall.model.vo.CartVO;

import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-15-21:24
 */
public interface CartService {


    List<CartVO> list(Integer userId);

    List<CartVO> addCart(Integer userId, Integer productId, Integer count);

    List<CartVO> update(Integer userId, Integer productId, Integer count);

    List<CartVO> delete(Integer userId, Integer productId);

    List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected);

    List<CartVO> selectAllOrNot(Integer userId, Integer selected);
}
