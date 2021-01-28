package com.lzf.mall.service.impl;

import com.lzf.mall.exception.MallException;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.model.dao.UserMapper;
import com.lzf.mall.model.pojo.User;
import com.lzf.mall.service.UserService;
import com.lzf.mall.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author lianzhengfeng
 * @create 2021-01-12-13:28
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    @Override
    public void selectByUserNameAndPassword(String userName, String password) throws MallException {
        //判断用户名是否重名
        User result = userMapper.selectByUserName(userName);
        if(result!=null){
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        //写入数据库
        User user=new User();
        user.setUsername(userName);
        //密码加密
        user.setPassword(MD5Utils.encode(password));
        int count = userMapper.insertSelective(user);
        if(count == 0){
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User doLogin(String userName, String password) throws MallException {
        String md5 = MD5Utils.encode(password);
        User user = userMapper.selectByLogin(userName, md5);
        if (user==null){
            throw new MallException(MallExceptionEnum.PASSWORD_FAILED);
        }
        return user;
    }
    @Override
    public void updateUserInfo(User user) throws MallException {
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count>1){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }

    }

    @Override
    public boolean checkAdminRole(User user){
       return user.getRole().equals(2);
    }
}
