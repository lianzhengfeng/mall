package com.lzf.mall.service;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.model.pojo.Category;
import com.lzf.mall.model.request.AddCategoryReq;
import com.lzf.mall.model.vo.CategoryVO;

import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-15-21:24
 */
public interface CategoryService {
    void  addCategory(AddCategoryReq addCategoryReq);

    void updadte(Category category);

    void delete(Integer id);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> listCategoryForCustomer(Integer parentId);
}
