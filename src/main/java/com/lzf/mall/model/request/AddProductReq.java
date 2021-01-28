package com.lzf.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author lianzhengfeng
 * @create 2021-01-20-13:46
 */
public class AddProductReq implements Serializable {
    @NotNull(message = "商品名称不能为空")
    private String name;
    @NotNull(message = "商品目录不能为空")
    private Integer categoryId;
    @NotNull(message = "价格不能为空")
    @Min(value = 1,message = "价格不能低于1分")
    private Integer price;
    @NotNull(message = "库存不能为空")
    @Max(value = 10000,message = "库存不能超过10000")
    private Integer stock;
    @NotNull(message = "商品详情不能为空")
    private String detail;
    @NotNull(message = "图片不能为空")
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "AddProductReq{" +
                "name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", stock=" + stock +
                ", detail='" + detail + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
