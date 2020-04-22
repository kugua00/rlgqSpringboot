package com.itdr.services;

import com.itdr.common.ServerResponse;

public interface OrderService {


    /**
     * 创建订单
     * @param uid
     * @param shippingId
     * @return
     */
    ServerResponse createOrder(Integer uid, Integer shippingId);

    /**
     * 获取订单详情信息
     * @param id
     * @param orderNo
     * @return
     */
    ServerResponse getOrderCartProduct(Integer id, Long orderNo);


    /**
     * 获取登录用户的订单列表
     * @param id
     * @return
     */
    ServerResponse listdo(Integer id, Integer pageSize, Integer pageNum);


    /**
     * 取消订单
     * @param id
     * @param orderNo
     * @return
     */
    ServerResponse countermandOrder(Integer id, Long orderNo);
}
