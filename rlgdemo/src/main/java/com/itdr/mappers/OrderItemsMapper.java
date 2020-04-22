package com.itdr.mappers;

import com.itdr.pojo.OrderItems;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItems record);

    int insertSelective(OrderItems record);

    OrderItems selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItems record);

    int updateByPrimaryKey(OrderItems record);

    //根据订单编号查询订单对应商品详情
    List<OrderItems> selectByOrderNo(Long orderNo);

    //往数据库中插入数据
    int insertAll(@Param("orderItemsList") List<OrderItems> orderItemsList);

    //根据用户id和订单编号获取对应的订单详情信息
    List<OrderItems> selectByUidAndOrderNo(@Param("id") Integer id, @Param("orderNo") Long orderNo);
}