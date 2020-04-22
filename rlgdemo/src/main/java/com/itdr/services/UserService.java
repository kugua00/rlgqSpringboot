package com.itdr.services;


import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;

public interface UserService {

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    ServerResponse<Users> login(String username, String password);

    /**
     * 用户注册
     * @param u
     * @return
     */
    ServerResponse<Users> register(Users u);


    /**
     * 检查用户名或者邮箱是否有效
     * @param str
     * @param type
     * @return
     */
    ServerResponse<Users> checkUserName(String str, String type);


    /**
     * 获取当前登录用户的详细信息
     * @param users
     * @return
     */
    ServerResponse getInforamtion(Users users);


    /**
     * 登录状态更新个人信息
     * @param users
     * @return
     */
    ServerResponse<Users> updateInformation(Users users);


    /**
     * 忘记密码
     * @param username
     * @return
     */
    ServerResponse<Users> forgetGetQuestion(String username);

    /**
     * 提交问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    ServerResponse<Users> forgetCheckAnswer(String username, String question, String answer);


    /**
     * 忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    ServerResponse<Users> forgetResetPassword(String username, String passwordNew, String forgetToken);


    /**
     * 登录状态中重置密码
     * @param users
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    ServerResponse<Users> resetPassword(Users users, String passwordOld, String passwordNew);

}
