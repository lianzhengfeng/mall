package com.lzf.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.common.R;
import com.lzf.mall.model.pojo.Product;
import com.lzf.mall.model.request.ProductListReq;
import com.lzf.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lianzhengfeng
 * @create 2021-01-20-16:54
 * 商品前台Controller
 */
@RestController
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;


    @ApiOperation("前台商品详情")
    @GetMapping("/product/detail")
    public R detail(@RequestParam("id") Integer id){
        Product product = productService.queryProductDetail(id);
        return R.success(product);
    }

    @ApiOperation("前台商品详情")
    @GetMapping("/product/list")
    public R list(ProductListReq productListReq){
        PageInfo pageInfo = productService.list(productListReq);
        return R.success(pageInfo);
    }

}
