package com.itdr.services;

import com.itdr.common.ServerResponse;

import java.util.Map;

public interface AliPayService {


    /**
     * 订单支付
     * @param orderNo
     * @param uid
     * @return
     */
    ServerResponse pay(Long orderNo, Integer uid);


    ServerResponse alipayCallback(Map<String, String> newmap);

    /**
     * 查询支付状态
     * @param orderno
     * @param id
     * @return
     */
    ServerResponse queryOrderPayStatus(Long orderno, Integer id);
}
