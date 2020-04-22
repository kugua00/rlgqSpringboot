package com.itdr.mappers;



import com.itdr.pojo.Products;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductsMapper {

    /**
     * 根据商品id获取商品详情
     * @param productId
     * @param is_new
     * @param is_hot
     * @param is_banner
     * @return
     */
    Products selectById(@Param("productId") Integer productId,
                        @Param("is_new") Integer is_new,
                        @Param("is_hot") Integer is_hot,
                        @Param("is_banner") Integer is_banner);



    /**
     * 根据商品id或者名称关键字进行查询
     * @param productId
     * @param keyWord
     * @param col
     * @param order
     * @return
     */
    List<Products> selectByIdOrName(@Param("productId") Integer productId,
                                    @Param("keyWord") String keyWord,
                                    @Param("col") String col,
                                    @Param("order") String order);


    /**
     * 根据商品ID获取商品数据
     * @param productId
     * @return
     */
    Products selectByProductId(Integer productId);

    /**
     * 根据商品Id更新商品数据
     * @param products
     * @return
     */
    int updateById(Products products);
}