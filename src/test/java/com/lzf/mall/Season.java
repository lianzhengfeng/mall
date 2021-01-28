package com.lzf.mall;

/**
 * @author lianzhengfeng
 * @create 2021-01-12-17:45
 */
public enum Season {
    NEED_USER_NAME(10001,"用户名不能为空");
    Integer code;
    String msg;

    Season(Integer code, String msg) {
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

    public static void main(String[] args) {
        Season season=Season.NEED_USER_NAME;
        System.out.println(season.values());
    }
}
