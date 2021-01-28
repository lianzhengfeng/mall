package com.lzf.mall.common;

import com.google.common.collect.Sets;
import com.lzf.mall.exception.MallException;
import com.lzf.mall.exception.MallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author lianzhengfeng
 * @create 2021-01-13-16:49
 * 常量类
 */
@Component
public class Constant {
    //加盐
    public static final  String SALT="XCSKLJDS_xm123@..";
    public static final String MALL_USER="mall_user";
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir){
        FILE_UPLOAD_DIR=fileUploadDir;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC= Sets.newHashSet("price desc","price asc");
    }

    public interface getProductStatus{
        int NOT_STATUS=0;//下架
        int STATUS=1;//上架
    }

    public interface Selected{
        int NOT_CHECKED=0;//未选中
        int CHECKED=1;//选中
    }

    public enum OrderStatusEnum{
        USER_CANCEL(0,"用户已取消"),
        Not_PAYING(10,"未付款"),
        PAYING(20,"已付款"),
        DELIVERY(30,"已发货"),
        FINISHED(40,"交易完成")
        ;
        private Integer code;

        private String value;

        OrderStatusEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(Integer code){
            for (OrderStatusEnum orderStatusEnum:values()){
                if (orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new MallException(MallExceptionEnum.NO_ENUM);
        }
        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
