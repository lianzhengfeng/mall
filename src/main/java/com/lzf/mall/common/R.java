package com.lzf.mall.common;

import com.lzf.mall.exception.MallExceptionEnum;

/**
 * @author lianzhengfeng
 * @create 2021-01-13-13:12
 */
public class R<T>{
    private Integer status;
    private String msg;
    private  T data;

    private static final int OK_CODE=10000;

    private static final String OK_MSG="SUCCESS";

    public R(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public R(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public R() {
        this(OK_CODE,OK_MSG);
    }

    public static <T> R<T> success() {
        return new R<>();
    }

    public static <T> R<T> success(T result) {
        R<T> response=new R<>();
        response.setData(result);
        return response;
    }

    public static <T> R<T> error(Integer code,String msg) {
        return new R<>(code,msg);
    }

    public static <T> R<T> error(MallExceptionEnum exceptionEnum) {
        return new R<>(exceptionEnum.getCode(),exceptionEnum.getMsg());
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static int getOkCode() {
        return OK_CODE;
    }

    public static String getOkMsg() {
        return OK_MSG;
    }

    @Override
    public String toString() {
        return "R{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
