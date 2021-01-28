package com.lzf.mall.model.dao;

import com.lzf.mall.model.pojo.Product;
import com.lzf.mall.model.query.ProductListQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    Product selectByName(String name);

    int batchUpdateProductIds(@Param("ids") Integer[] ids,@Param("productStatus") Integer productStatus);

    List<Product> selectAll();

    List<Product> selectList(@Param("query") ProductListQuery query);
}