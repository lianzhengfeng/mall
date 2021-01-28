package com.lzf.mall.model.query;

import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-21-13:40
 * 查询商品列表的Query
 */
public class ProductListQuery {
    private String keyword;

    private List<Integer> categoryIds;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
