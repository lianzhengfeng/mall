package com.lzf.mall.exception;

/**
 * @author lianzhengfeng
 * @create 2021-01-13-13:28
 */
//异常枚举
public enum  MallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    PASSWORD_LENGTH(10003,"密码长度不能低于8位"),
    NAME_EXISTED(10004,"用户名已存在,注册失败"),
    INSERT_FAILED(10005,"插入失败,请重试"),
    PASSWORD_FAILED(10006,"密码错误"),
    LOGIN_FAILED(10007,"尚未登录"),
    UPDATE_FAILED(10008,"更新失败"),
    NEED_ADMIN(10009,"无管理员权限"),
    NEED_CATEGORY_NAME(10010,"不能出现重复名字"),
    REQUEST_PARAM_ERROR(10011,"参数错误"),
    DELETE_FAILED(10012,"删除失败"),
    MKDIR_FILE(10013,"文件夹创建失败"),
    UPLOAD_FAILED(10014,"图片上传失败"),
    PRODUCT_FAILED(10015,"商品出错"),
    PRODUCT_STOCK(10016,"商品库存不足"),
    CART_FAILED(10017,"购物车已勾选的商品为空"),
    NO_ENUM(10018,"为找到对应的枚举类"),
    NO_ORDER(10019,"订单不存在"),
    ORDER_NO_YOUR(10020,"该订单不属于你"),
    ORDER_STATUS_ERROR(10021,"订单状态异常"),
    SYSTEM_ERROR(20000,"系统异常");
    /**
     * 异常码
     */
    Integer code;
    /**
     * 异常信息
     */
    String msg;

    MallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
