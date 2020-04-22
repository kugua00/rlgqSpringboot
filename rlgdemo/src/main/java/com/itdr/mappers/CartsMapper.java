package com.itdr.mappers;

import com.itdr.pojo.Carts;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CartsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Carts record);

    int insertSelective(Carts record);

    Carts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Carts record);

    int updateByPrimaryKey(Carts record);

    //根据用户id和商品id判断数据是否存在
    Carts selectByUidAndProductId(@Param("uid") Integer uid, @Param("productId") Integer productId);

    //根据用户id查询所有购物车数据
    List<Carts> selectByUid(Integer uid);

    //根据用户ID判断用户购物车是否全选
    int selectByUidCheck(@Param("uid") Integer uid, @Param("check") Integer check);

    //根据用户id和商品id删除对应购物车数据
    int deleteByProductIds(@Param("productList") List<String> productList, @Param("id") Integer id);

    //根据用户id将该用户的订单的选中状态
    int updateByUid(@Param("id") Integer id, @Param("check") Integer check, @Param("productId") Integer productId);

    //根据用户id查询选中的的订单
    List<Carts> selectByUidAll(Integer uid);

    //批量删除
    int deleteAllByIdAndUid(@Param("li") List<Carts> cartsList, @Param("uid") Integer uid);
}