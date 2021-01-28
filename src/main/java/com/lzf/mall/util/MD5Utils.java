package com.lzf.mall.util;

import com.lzf.mall.common.Constant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lianzhengfeng
 * @create 2021-01-13-16:36
 * 加密工具
 */
public class MD5Utils {
    public static String encode(String strVal){
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            //这里加入盐
            byte[] array = md.digest((strVal+ Constant.SALT).getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte bt : array) {
                sb.append(Integer.toHexString((bt & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception ex) {
        }
        return null;
    }

    public static void main(String[] args) {
        String md5=encode("123");
        System.out.println(md5);
    }
}
