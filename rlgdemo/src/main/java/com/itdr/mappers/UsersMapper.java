package com.itdr.mappers;

import com.itdr.pojo.Users;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    //根据账号和密码查询用户
    Users selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);


    //根据用户名或者邮箱查询用户
    int selectByUserNameOrEmail(@Param("str") String str, @Param("type") String type);

    //根据邮箱查询有几条数据
    int selectByEmailAndId(@Param("email") String email, @Param("email") Integer id);


    //根据账号查询用户密码问题
    String selectByUserName(String username);

    //根据用户名和问题和答案查询用户是否存在
    int selectByUserNameAndQuestionAndAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    //根据用户名去更新密码
    int updateByUserNameAndPassword(@Param("username") String username, @Param("passwordNew") String passwordNew);

    //根据用户id查询密码是否正确
    int selectByIdAndPassword(@Param("id") Integer id, @Param("passwordOld") String passwordOld);
}