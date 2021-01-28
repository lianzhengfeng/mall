package com.lzf.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.common.Constant;
import com.lzf.mall.common.R;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.model.pojo.Category;
import com.lzf.mall.model.pojo.User;
import com.lzf.mall.model.request.AddCategoryReq;
import com.lzf.mall.model.request.UpdateCategoryReq;
import com.lzf.mall.model.vo.CategoryVO;
import com.lzf.mall.service.CategoryService;
import com.lzf.mall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-15-21:17
 * 目录分类
 */
@RestController
@CrossOrigin
public class CategoryController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 添加目录分类
     * @param session
     * @param addCategoryReq
     * @return
     */
    @PostMapping("/admin/category/add")
    public R addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq){
        User user=(User)session.getAttribute(Constant.MALL_USER);
        //判断用户是否登录
        if (user==null){
            return R.error(MallExceptionEnum.LOGIN_FAILED);
        }
        //判断用户身份是否是管理员
        if (userService.checkAdminRole(user)) {
            categoryService.addCategory(addCategoryReq);
            return R.success();
        }else{
            return R.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

    @PostMapping("/admin/category/update")
    public R updateCategory(HttpSession session, @Valid @RequestBody UpdateCategoryReq updateCategoryReq){
        User user=(User)session.getAttribute(Constant.MALL_USER);
        //判断用户是否登录
        if (user==null){
            return R.error(MallExceptionEnum.LOGIN_FAILED);
        }
        //判断用户身份是否是管理员
        if (userService.checkAdminRole(user)) {
            Category category=new Category();
            BeanUtils.copyProperties(updateCategoryReq,category);
            categoryService.updadte(category);
            return R.success();
        }else{
            return R.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

    @PostMapping("/admin/category/delete")
    public R deleteCategory(@RequestParam Integer id){
        categoryService.delete(id);
        return R.success();
    }

    @GetMapping("/admin/category/list")
    public R listCategoryForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return R.success(pageInfo);
    }

    @GetMapping("/category/list")
    public R listCategoryForCustomer(){
        List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(0);
        return R.success(categoryVOList);
    }
}
