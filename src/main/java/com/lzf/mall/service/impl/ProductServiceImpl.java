package com.lzf.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lzf.mall.common.Constant;
import com.lzf.mall.common.R;
import com.lzf.mall.exception.MallException;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.model.dao.ProductMapper;
import com.lzf.mall.model.pojo.Product;
import com.lzf.mall.model.query.ProductListQuery;
import com.lzf.mall.model.request.AddProductReq;
import com.lzf.mall.model.request.ProductListReq;
import com.lzf.mall.model.request.UpdateCategoryReq;
import com.lzf.mall.model.request.UpdateProductReq;
import com.lzf.mall.model.vo.CategoryVO;
import com.lzf.mall.service.CategoryService;
import com.lzf.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lianzhengfeng
 * @create 2021-01-20-13:45
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;
    @Override
    public void addProduct(AddProductReq addProductReq){
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq,product);
        Product productName=productMapper.selectByName(addProductReq.getName());
        if (productName!=null){
            throw new MallException(MallExceptionEnum.NEED_CATEGORY_NAME);
        }
        int count = productMapper.insertSelective(product);
        if (count == 0){
            throw  new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public void updateProduct(Product product){
        Product productOld = productMapper.selectByName(product.getName());
        //同名且不同id,不能继续修改
        if (productOld!=null && !product.getId().equals(productOld.getId())){
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count == 0){
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void deleteProduct(Integer id){
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0){
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        }
    }
    @Override
    public void batchUpdateProductStatus(Integer[] ids, Integer productStatus){
        productMapper.batchUpdateProductIds(ids,productStatus);
    }

    @Override
    public PageInfo productListForAdmin(Integer pageNum, Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectAll();
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    @Override
    public Product queryProductDetail(Integer id){
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq){
        //构建Query对象
        ProductListQuery productListQuery = new ProductListQuery();
        //搜索处理
        if (!StringUtils.isEmpty(productListReq.getKeyword())){
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(keyword);
        }
        //目录处理
        if (productListReq.getCategoryId() !=null){
            List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds=new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCateogryIds(categoryVOList,categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }

        //排序处理
        String orderBy = productListReq.getOrderBy();
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize(),orderBy);
        }else{
            PageHelper.startPage(productListReq.getPageNum(),productListReq.getPageSize());
        }
        List<Product> products = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(products);
        return pageInfo;
    }

    private void getCateogryIds(List<CategoryVO> categoryVOList,ArrayList<Integer> categoryIds){
        for (CategoryVO     categoryVO : categoryVOList) {
            if (categoryVO!=null){
                categoryIds.add(categoryVO.getId());
                getCateogryIds(categoryVO.getChildCategory(),categoryIds);
            }
        }
    }
}
