package com.lzf.mall.exception;

/**
 * @author lianzhengfeng
 * @create 2021-01-13-15:12
 * 统一异常
 */
public class MallException extends RuntimeException{

   private final Integer code;
   private final String message;


    public MallException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }



    public MallException(MallExceptionEnum mallExceptionEnum) {
        this(mallExceptionEnum.getCode(),mallExceptionEnum.getMsg());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
