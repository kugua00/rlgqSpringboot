package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Products;

public interface ProductService {


    /**
     * 获取产品分类
     * @param pid
     * @return
     */
    ServerResponse<Products> topcategory(Integer pid);

    /**
     * 获取商品详情
     * @param productId
     * @param is_new
     * @param is_hot
     * @param is_banner
     * @return
     */
    ServerResponse<Products> detail(Integer productId, Integer is_new, Integer is_hot, Integer is_banner);

    /**
     * 商品搜索 + 动态排序
     * @param productId
     * @param keyWord
     * @param pageNum
     * @param pageSize
     * @param orderBy
     * @return
     */
    ServerResponse<Products> listProduct(Integer productId, String keyWord, Integer pageNum, Integer pageSize, String orderBy);

}
