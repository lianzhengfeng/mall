package com.lzf.mall.controller;

import com.lzf.mall.common.Constant;
import com.lzf.mall.common.R;
import com.lzf.mall.exception.MallException;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.model.pojo.User;
import com.lzf.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author lianzhengfeng
 * @create 2021-01-12-13:26
 */
@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public User getUser(){
        return userService.getUser();
    }

    /**
     * 注册
     * @param userName
     * @param password
     * @return
     * @throws MallException
     */
    @PostMapping("/register")
    public R register(@RequestParam("userName") String userName,@RequestParam("password") String password) throws MallException {
        //判断用户名和密码是否为空
        if(StringUtils.isEmpty(userName)){
            return R.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return R.error(MallExceptionEnum.NEED_PASSWORD);
        }
        //判断密码长度是否小于8
        if (password.length()<8){
            return R.error(MallExceptionEnum.PASSWORD_LENGTH);
        }
        userService.selectByUserNameAndPassword(userName,password);
        return R.success();
    }

    /**
     * 登录
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws MallException
     */
    @PostMapping("/login")
    public R login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws MallException {
        //判断用户名和密码是否为空
        if(StringUtils.isEmpty(userName)){
            return R.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return R.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.doLogin(userName, password);
        //保存用户信息时不保存密码
        user.setPassword(null);
        //保存用户信息到Session
        session.setAttribute(Constant.MALL_USER,user);
        return R.success(user);
    }

    /**
     * 更新个性签名
     * @param session
     * @param signature
     * @return
     * @throws MallException
     */
    @PostMapping("/user/update")
    public R updateUserInfo(HttpSession session,@RequestParam("signature") String signature) throws MallException {
        User currentUser =(User)session.getAttribute(Constant.MALL_USER);
        if (currentUser==null){
            return R.error(MallExceptionEnum.LOGIN_FAILED);
        }
        User user=new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateUserInfo(user);
        return R.success();
    }

    @PostMapping("/user/logout")
    public R logOut(HttpSession session){
        session.removeAttribute(Constant.MALL_USER);
        return R.success();
    }


    /**
     * 管理员登录
     * @param userName
     * @param password
     * @param session
     * @return
     * @throws MallException
     */
    @PostMapping("/adminLogin")
    public R adminLogin(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws MallException {
        //判断用户名和密码是否为空
        if(StringUtils.isEmpty(userName)){
            return R.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if(StringUtils.isEmpty(password)){
            return R.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.doLogin(userName, password);
        //检测是否是管理员
        if (userService.checkAdminRole(user)) {
            //保存用户信息时不保存密码
            user.setPassword(null);
            //保存用户信息到Session
            session.setAttribute(Constant.MALL_USER,user);
            return R.success(user);
        }else{
            return R.error(MallExceptionEnum.NEED_ADMIN);
        }
    }
}
