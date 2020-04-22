package com.itdr.mappers;

import com.itdr.pojo.Shippings;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shippings record);

    int insertSelective(Shippings record);

    Shippings selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shippings record);

    int updateByPrimaryKey(Shippings record);

    //根据用户查询收货地址
    List<Shippings> selectByKey(Integer id);

    //根据收货地址id 和用户id 查询信息
    Shippings selectByIdAndUid(@Param("shippingId") Integer shippingId, @Param("uid") Integer uid);
}