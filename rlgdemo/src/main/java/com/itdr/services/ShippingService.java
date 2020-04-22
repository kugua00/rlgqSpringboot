package com.itdr.services;

import com.itdr.common.ServerResponse;
import com.itdr.pojo.Shippings;

public interface ShippingService {

    /**
     * 查询收货地址
     * @param id
     * @return
     */
    ServerResponse<Shippings> listdo(Integer id);


    /**
     * 添加收货地址
     * @param shippings
     * @return
     */
    ServerResponse<Shippings> insertdo(Shippings shippings);

    /**
     * 删除信息
     * @param shippings
     * @return
     */
    ServerResponse<Shippings> deletedo(Shippings shippings);

    /**
     * 修改收货地址
     * @param shippings
     * @return
     */
    ServerResponse<Shippings> updatedo(Shippings shippings);
}
