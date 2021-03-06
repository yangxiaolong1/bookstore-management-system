package com.bookstore.controller;

import com.bookstore.bean.MyPageInfo;
import com.bookstore.bean.ProductImage;
import com.bookstore.service.ProductImageService;
import com.bookstore.util.FileUploadUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Map;

/**
 * Created by SX-503 on 2018/9/20.
 */
@RestController
@RequestMapping("/api/productImage")
public class ProductImageController extends BaseApiController{

    @Autowired ProductImageService productImageService;

    @Autowired
    FileUploadUtils fileUploadUtils;

    // 增
    @PostMapping("/add")
    public Map<String, Object> add(@RequestParam(required = false)CommonsMultipartFile file, @RequestParam Long product_id) {

        if (file == null) return onBadResp("picture 不能为空");
        if (product_id == null) return onBadResp("subtitle 不能为空");

        String filePath = "";

        ProductImage productImage = new ProductImage();
        productImage.setProductId(product_id);

        if (file != null && !file.isEmpty()) {
            filePath = fileUploadUtils.getWordPath(file);
            if (filePath == null) return onBadResp("该文件不符合格式");
            productImage.setPicture(fileUploadUtils.getBasePath() + filePath);
        }
        if (productImageService.insert(productImage) > 0) {
            if (StringUtils.isNotEmpty(filePath)) fileUploadUtils.saveFile(file, filePath);
            return onSuccessRep("添加成功");
        }
        return onBadResp("添加失败");
    }

    // 批量删
    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestParam Long[] id, HttpSession session){
        String picture = productImageService.selectById(id).getPicture();
        File file = new File(picture);
        if (productImageService.deleteBatch(id) > 0 && file.delete()) {
            return onSuccessRep("删除成功");
        }
        return onBadResp("删除失败");
    }

    // 改
    @PostMapping("/update")
    public Map<String, Object> update(@RequestParam Long id, @RequestParam(required = false)CommonsMultipartFile file, Long product_id )
    {
        String picture = productImageService.selectById(id).getPicture();
        File file1 = new File(picture);

        ProductImage productImage = new ProductImage();
        productImage.setId(id);
        if (product_id != null) productImage.setProductId(product_id);

        String filePath = "";

        if (file != null && !file.isEmpty()) {
            filePath = fileUploadUtils.getWordPath(file);
            if (filePath == null) return onBadResp("该文件不符合格式");
            productImage.setPicture(fileUploadUtils.getBasePath() + filePath);
        }
        if (productImageService.updateById(productImage) > 0 && file1.delete()) {
            if (StringUtils.isNotEmpty(filePath)) fileUploadUtils.saveFile(file, filePath);
            return onSuccessRep("修改成功");
        }
        return onSuccessRep("修改失败");
    }

    // 查
    @GetMapping("/list")
    public MyPageInfo<ProductImage> select(@RequestParam(required = true,defaultValue = "1") Integer pageNo, @RequestParam(required = false, defaultValue = "10") Integer pageSize)
    {
        PageHelper.startPage(pageNo,pageSize);
        return new MyPageInfo<ProductImage>(productImageService.select());
    }
}
