package com.lzf.mall.service;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.model.pojo.Category;
import com.lzf.mall.model.pojo.Product;
import com.lzf.mall.model.request.AddCategoryReq;
import com.lzf.mall.model.request.AddProductReq;
import com.lzf.mall.model.request.ProductListReq;
import com.lzf.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-15-21:24
 */
public interface ProductService {

    void addProduct(AddProductReq addProductReq);

    void updateProduct(Product product);

    void deleteProduct(Integer id);

    void batchUpdateProductStatus(Integer[] ids, Integer productStatus);

    PageInfo productListForAdmin(Integer pageNum, Integer pageSize);


    Product queryProductDetail(Integer id);

    PageInfo list(ProductListReq productListReq);
}
