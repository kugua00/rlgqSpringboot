package com.itdr.services.impl;

import com.itdr.common.ServerResponse;
import com.itdr.mappers.ShippingsMapper;
import com.itdr.pojo.Shippings;
import com.itdr.services.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingServiceImpl implements ShippingService {


    @Autowired
    ShippingsMapper shippingsMapper;

    /**
     * 查询收货地址
     * @param id
     * @return
     */
    @Override
    public ServerResponse<Shippings> listdo(Integer id) {
        List<Shippings> li =  shippingsMapper.selectByKey(id);
        return ServerResponse.successRS(li);
    }


    /**
     * 添加收货地址
     * @param shippings
     * @return
     */
    @Override
    public ServerResponse<Shippings> insertdo(Shippings shippings) {

        int insert = shippingsMapper.insert(shippings);
        if (insert <= 0){
            return ServerResponse.defeatedRs("添加失败");
        }
        return ServerResponse.successRS("添加成功");
    }

    @Override
    public ServerResponse<Shippings> deletedo(Shippings shippings) {

        int i = shippingsMapper.deleteByPrimaryKey(shippings.getId());
        if (i <= 0){
            return ServerResponse.defeatedRs("删除失败");
        }
        return ServerResponse.successRS("删除成功");
    }

    @Override
    public ServerResponse<Shippings> updatedo(Shippings shippings) {

        int i = shippingsMapper.updateByPrimaryKeySelective(shippings);
        if (i <= 0){
            return ServerResponse.defeatedRs("更新失败");
        }
        return ServerResponse.successRS("更新成功");
    }
}
