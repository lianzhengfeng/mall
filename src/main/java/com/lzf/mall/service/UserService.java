package com.lzf.mall.service;

import com.lzf.mall.exception.MallException;
import com.lzf.mall.model.pojo.User;

/**
 * @author lianzhengfeng
 * @create 2021-01-12-13:27
 */
public interface UserService {
    User getUser();
    void selectByUserNameAndPassword(String userName,String password) throws MallException;

    User doLogin(String userName, String password) throws MallException;

    void updateUserInfo(User user) throws MallException;

    boolean checkAdminRole(User user);
}
