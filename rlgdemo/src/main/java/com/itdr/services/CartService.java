package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.vo.CartVO;

public interface CartService {


    /**
     *  购物车添加商品
     * @param productId
     * @param count
     * @param uid
     * @return
     */
    ServerResponse<CartVO> addOne(Integer productId, Integer count, Integer uid);

    /**
     * 获取登录用户购物车列表
     * @param id
     * @return
     */
    ServerResponse<CartVO> listCart(Integer id);

    /**
     * 更新购物车某个产品数量
     * @param productId
     * @param count
     * @param id
     * @return
     */
    ServerResponse<CartVO> updateCart(Integer productId, Integer count, Integer id);


    /**
     * 移除购物车某个产品
     * @param productIds
     * @param id
     * @return
     */
    ServerResponse<CartVO> deleteCart(String productIds, Integer id);


    /**
     *查询购物车里的商品信息条数
     * @param id
     * @return
     */
    ServerResponse<Integer> getCartProductCount(Integer id);


    /**
     * 购物车全选
     * @param id
     * @return
     */
    ServerResponse<CartVO> selectAll(Integer id, Integer check, Integer productId);
}
