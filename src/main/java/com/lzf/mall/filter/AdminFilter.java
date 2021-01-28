package com.lzf.mall.filter;

import com.lzf.mall.common.Constant;
import com.lzf.mall.common.R;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.model.pojo.Category;
import com.lzf.mall.model.pojo.User;
import com.lzf.mall.service.CategoryService;
import com.lzf.mall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author lianzhengfeng
 * @create 2021-01-18-19:14
 * 统一处理管理员权限
 */
public class AdminFilter implements Filter {
    @Autowired
    private UserService userService;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        User user=(User)session.getAttribute(Constant.MALL_USER);
        //判断用户是否登录
        if (user==null){
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"LOGIN_FAILED\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
        //判断用户身份是否是管理员
        if (userService.checkAdminRole(user)) {
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\": 10009,\n" +
                    "    \"msg\": \"NEED_ADMIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
        }

    }

    @Override
    public void destroy() {

    }
}
