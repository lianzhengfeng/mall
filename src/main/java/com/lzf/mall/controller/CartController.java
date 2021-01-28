package com.lzf.mall.controller;

import com.lzf.mall.common.R;
import com.lzf.mall.filter.UserFilter;
import com.lzf.mall.model.pojo.User;
import com.lzf.mall.model.vo.CartVO;
import com.lzf.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-22-14:38
 * 购物车模块
 */
@RestController
@CrossOrigin
public class CartController {
    @Autowired
    CartService cartService;
    /**
     *
     * @param productId 商品id
     * @param count 商品数量
     * @return
     */
    @PostMapping("/cart/add")
    public R addCart(@RequestParam Integer productId,@RequestParam Integer  count){
        List<CartVO> cartVOS = cartService.addCart(UserFilter.currentUser.getId(), productId, count);
        return R.success(cartVOS);
    }

    @GetMapping("/cart/list")
    public R list(){
        //内部获取用户信息
        List<CartVO> cartVOS = cartService.list(UserFilter.currentUser.getId());
        return R.success(cartVOS);
    }


    @PostMapping("/cart/update")
    public R updateCart(@RequestParam Integer productId,@RequestParam Integer  count){
        List<CartVO> cartVOS = cartService.update(UserFilter.currentUser.getId(),productId,count);
        return R.success(cartVOS);
    }

    @PostMapping("/cart/delete")
    public R deleteCart(@RequestParam Integer productId){
        //不能传入userId,cartId,否则可以删除别人的购物车
        List<CartVO> cartVOS = cartService.delete(UserFilter.currentUser.getId(),productId);
        return R.success(cartVOS);
    }

    @PostMapping("/cart/select")
    public R select(@RequestParam Integer productId,@RequestParam Integer selected){
        List<CartVO> cartVOS = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return R.success(cartVOS);
    }

    @PostMapping("/cart/selectAll")
    public R selectAll(@RequestParam Integer selected){
        List<CartVO> cartVOS = cartService.selectAllOrNot(UserFilter.currentUser.getId(),selected);
        return R.success(cartVOS);
    }
}
