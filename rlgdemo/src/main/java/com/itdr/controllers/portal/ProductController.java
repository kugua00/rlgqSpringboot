package com.itdr.controllers.portal;


import com.itdr.common.ServerResponse;
import com.itdr.pojo.Products;
import com.itdr.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/portal/product/")
public class ProductController {

    @Autowired
    ProductService productService;


    /**
     * 获取产品分类
     * @param pid
     * @return
     */
    @RequestMapping("topcategory.do")
    public ServerResponse<Products> topcategory(@RequestParam(value = "pid",required = false,defaultValue = "0")Integer pid){
        return productService.topcategory(pid);
    }


    /**
     * 获取商品详情
     * @param productId
     * @param is_new
     * @param is_hot
     * @param is_banner
     * @return
     */
    @RequestMapping("detail.do")
    public ServerResponse<Products> detail(Integer productId,
                                           @RequestParam(value = "is_new",required = false,defaultValue = "0") Integer is_new,
                                           @RequestParam(value = "is_hot",required = false,defaultValue = "0") Integer is_hot,
                                           @RequestParam(value = "is_banner",required = false,defaultValue = "0") Integer is_banner){
        return productService.detail(productId,is_new,is_hot,is_banner);
    }


    /**
     * 商品搜索 + 动态排序
     * @param productId
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    @RequestMapping("list.do")
    public ServerResponse<Products> listProduct(Integer productId, String keyWord,
                                                @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
                                                @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
                                                @RequestParam(value = "orderBy",required = false,defaultValue = "")String orderBy){
        return productService.listProduct(productId,keyWord,pageNum,pageSize,orderBy);
    }
}
