package com.itdr.mappers;

import com.itdr.pojo.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

    //根据订单编号查询订单
    Orders selectByOrderNo(Long orderNo);

    //根据订单编号和用户id查询订单是否存在
    int selectByOrderNoAndUid(@Param("orderNo") Long orderNo, @Param("uid") Integer uid);

    //根据用户id获取该用户的所有订单
    List<Orders> selectByUid(Integer id);

    //改变订单状态
    int updateToStatus(Orders orders);
}