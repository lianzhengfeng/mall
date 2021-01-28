package com.lzf.mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzf.mall.exception.MallException;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.model.dao.CategoryMapper;
import com.lzf.mall.model.pojo.Category;
import com.lzf.mall.model.request.AddCategoryReq;
import com.lzf.mall.model.request.UpdateCategoryReq;
import com.lzf.mall.model.vo.CategoryVO;
import com.lzf.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-15-21:24
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void  addCategory(AddCategoryReq addCategoryReq){
        Category category=new Category();
        BeanUtils.copyProperties(addCategoryReq,category);
        //先判断是否有重名目录
        Category checkCategoryName = categoryMapper.selectByName(addCategoryReq.getName());
        if (checkCategoryName!=null){
            throw new MallException(MallExceptionEnum.NEED_CATEGORY_NAME);
        }else{
            int count = categoryMapper.insertSelective(category);
            if (count==0){
                throw new MallException(MallExceptionEnum.INSERT_FAILED);
            }
        }
    }

    @Override
    public void updadte(Category category){
        //先判断是否有重名
        Category checkCategoryName = categoryMapper.selectByName(category.getName());
        if (checkCategoryName!=null && !category.getId().equals(checkCategoryName.getId())){
            throw new MallException(MallExceptionEnum.NEED_CATEGORY_NAME);
        }
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count ==0 ){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id){
        Category selectByPrimaryKey = categoryMapper.selectByPrimaryKey(id);
        if (selectByPrimaryKey == null){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum, pageSize, "type,order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo=new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "listCategoryForCustomer")
    public List<CategoryVO> listCategoryForCustomer(Integer parentId){
        List<CategoryVO> categoryVOList=new ArrayList<>();
        recursionCategoryCustomer(categoryVOList,parentId);
        return categoryVOList;
    }

    public void recursionCategoryCustomer(List<CategoryVO> categoryVOS,Integer parentId){
        List<Category> categories = categoryMapper.selectByParentId(parentId);
        if (!CollectionUtils.isEmpty(categories)){
            for (Category category : categories) {
                CategoryVO categoryVO=new CategoryVO();
                BeanUtils.copyProperties(category,categoryVO);
                categoryVOS.add(categoryVO);
                recursionCategoryCustomer(categoryVO.getChildCategory(),categoryVO.getId());
            }
        }
    }
}
