package com.lzf.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author lianzhengfeng
 * @create 2021-01-15-21:18
 */
public class AddCategoryReq {
    @Size(min = 2,max = 6)
    @NotNull(message = "name不能为空")
    private String name;
    @Max(3)
    @NotNull(message = "type不能为空")
    private Integer type;
    @NotNull(message = "parentId不能为空")
    private Integer parentId;
    @NotNull(message = "orderNum不能为空")
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }
}
