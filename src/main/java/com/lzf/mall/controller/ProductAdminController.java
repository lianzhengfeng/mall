package com.lzf.mall.controller;

import com.github.pagehelper.PageInfo;
import com.lzf.mall.common.Constant;
import com.lzf.mall.common.R;
import com.lzf.mall.exception.MallException;
import com.lzf.mall.exception.MallExceptionEnum;
import com.lzf.mall.model.pojo.Product;
import com.lzf.mall.model.request.AddProductReq;
import com.lzf.mall.model.request.UpdateProductReq;
import com.lzf.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @author lianzhengfeng
 * @create 2021-01-20-13:44
 * 商品后台Controller
 */
@RestController
@CrossOrigin
public class ProductAdminController {
    @Autowired
    private ProductService productService;

    @PostMapping("/admin/product/add")
    public R addAdminProduct(@Valid @RequestBody AddProductReq addProductReq){
        productService.addProduct(addProductReq);
        return R.success();
    }

    @PostMapping("/admin/upload/file")
    public R upload(HttpServletRequest request,@RequestParam("file") MultipartFile file){
        //获取文件名称
        String fileName = file.getOriginalFilename();
        //后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //新文件名
        String newFileName=UUID.randomUUID().toString()+suffixName;
        //创建文件夹
        File fileDirectory=new File(Constant.FILE_UPLOAD_DIR);
        //目标文件
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);
        if (!fileDirectory.exists()){
            if (!fileDirectory.mkdir()){
                throw new MallException(MallExceptionEnum.MKDIR_FILE);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return R.success(getHost(new URI(request.getRequestURL()+""))+"/images/"+newFileName);
        } catch (URISyntaxException e) {
           return R.error(MallExceptionEnum.UPLOAD_FAILED);
        }
    }

    //获取IP和端口号
    private URI getHost(URI uri){
        URI effectiveURI;
        try {
            effectiveURI=new URI(uri.getScheme(),uri.getUserInfo(),uri.getHost(),uri.getPort(),null,null,null);
        } catch (URISyntaxException e) {
            effectiveURI=null;
        }
        return effectiveURI;
    }

    @PostMapping("/admin/product/update")
    public R updateProduct(@RequestBody UpdateProductReq updateProductReq){
        Product product=new Product();
        BeanUtils.copyProperties(updateProductReq,product);
        productService.updateProduct(product);
        return R.success();
    }

    @PostMapping("/admin/product/delete")
    public R deleteProduct(@RequestParam Integer id){
        productService.deleteProduct(id);
        return R.success();
    }
    @ApiOperation("后台商品批量上下架")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    public R batchUpdateProductStatus(@RequestParam("ids") Integer[] ids,@RequestParam("sellStatus") Integer sellStatus){
        productService.batchUpdateProductStatus(ids,sellStatus);
        return R.success();
    }

    @GetMapping("/admin/product/list")
    public R batchUpdateProductStatus(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize){
        PageInfo pageInfo = productService.productListForAdmin(pageNum, pageSize);
        return R.success(pageInfo);
    }

}
