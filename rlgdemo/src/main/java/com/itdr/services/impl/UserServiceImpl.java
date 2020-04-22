package com.itdr.services.impl;

import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.common.TokenCache;
import com.itdr.mappers.UsersMapper;
import com.itdr.pojo.Users;
import com.itdr.services.UserService;
import com.itdr.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UsersMapper usersMapper;



    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<Users> login(String username, String password) {
        if (username == null || username.equals("")){
            return ServerResponse.defeatedRs("用户名不能为空");
        }
        if (password == null || password.equals("")){
            return ServerResponse.defeatedRs("密码不能为空");
        }


        //根据用户名查找是否存在该用户
        int i = usersMapper.selectByUserNameOrEmail(username,"username");
        if (i <= 0){
            return ServerResponse.defeatedRs("该账号不存在");
    }

        //根据用户名和密码(*先加密)查询用户是否存在
        Users users = usersMapper.selectByUsernameAndPassword(username, MD5Utils.getMD5Code(password));

        if (users == null){
            return ServerResponse.defeatedRs("用户不存在");
        }


        //封装数据并返回
        ServerResponse sr = ServerResponse.successRS(users);
        return sr;
    }

    /**
     * 用户注册
     * @param u
     * @return
     */
    @Override
    public ServerResponse<Users> register(Users u) {
        if(u.getUsername() == null || u.getUsername().equals("")){
            return ServerResponse.defeatedRs("账户名不能为空");
        }
        if (u.getPassword() == null || u.getPassword().equals("")){
            return ServerResponse.defeatedRs("密码不能为空");
        }

        //检查注册用户是否存在
        int i = usersMapper.selectByUserNameOrEmail(u.getUsername(),"username");
        if (i > 0){
            return ServerResponse.defeatedRs("要注册的账号已存在");
        }

        u.setPassword(MD5Utils.getMD5Code(u.getPassword()));
        int insert = usersMapper.insert(u);
        if (insert != 1){
            return ServerResponse.defeatedRs("注册失败");
        }

        return ServerResponse.successRS("注册成功");
    }


    /**
     * 检查用户名或者邮箱是否有效
     * @param str
     * @param type
     * @return
     */
    @Override
    public ServerResponse<Users> checkUserName(String str, String type) {
        if (str == null || str.equals("")){
            return ServerResponse.defeatedRs("参数不能为空");
        }
        if (type == null || type.equals("")){
            return ServerResponse.defeatedRs("参数类型不能为空");
        }

        int i = usersMapper.selectByUserNameOrEmail(str,type);
        if (i > 0 && type.equals("username")){
            return ServerResponse.defeatedRs("用户名已存在");
        }
        if (i > 0 && type.equals("email")){
            return ServerResponse.defeatedRs("邮箱已存在");
        }
        return ServerResponse.successRS("校验成功");
    }


    /**
     * 获取当前登录用户的详细信息
     * @param users
     * @return
     */
    @Override
    public ServerResponse getInforamtion(Users users) {

        Users u = usersMapper.selectByPrimaryKey(users.getId());

        if (u == null ){
            return ServerResponse.defeatedRs("用户不存在");
        }
        u.setPassword("");
        return ServerResponse.successRS(u);
    }


    /**
     * 登录状态更新个人信息
     * @param users
     * @return
     */
    @Override
    public ServerResponse<Users> updateInformation(Users users) {
        int count = usersMapper.selectByEmailAndId(users.getEmail(),users.getId());
        if (count > 0){
            return ServerResponse.defeatedRs("要更改的邮箱已存在");
        }

        int i = usersMapper.updateByPrimaryKeySelective(users);
        if (i != 1){
            return ServerResponse.defeatedRs(104,"更新失败");
        }else {
            return ServerResponse.successRS(103,"更新成功");
        }

    }


    /**
     * 忘记密码
     * @param username
     * @return
     */
    @Override
    public ServerResponse<Users> forgetGetQuestion(String username) {
        if (username == null || username.equals("")){
            return ServerResponse.defeatedRs("参数不能为空");
        }

        int i = usersMapper.selectByUserNameOrEmail(username, Const.USERNAME);

        if (i <= 0){
             return ServerResponse.defeatedRs("用户名不存在");
        }

        String question = usersMapper.selectByUserName(username);
        if (question == null || question.equals("")){
            return ServerResponse.defeatedRs("该用户未设置密码问题");
        }
        return ServerResponse.successRS(question);
    }

    /**
     * 提交问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @Override
    public ServerResponse<Users> forgetCheckAnswer(String username, String question, String answer) {
        if (username == null || username.equals("")){
            return ServerResponse.defeatedRs("用户名不能为空");
        }
        if (question == null || question.equals("")){
            return ServerResponse.defeatedRs("问题不能为空");
        }
        if (answer == null || answer.equals("")){
            return ServerResponse.defeatedRs("答案不能为空");
        }


        int i = usersMapper.selectByUserNameAndQuestionAndAnswer(username,question,answer);
        if (i <= 0 ){
            return ServerResponse.defeatedRs("问题答案不匹配");
        }

        //令牌  利用缓存
        String token = UUID.randomUUID().toString();
        //把令牌放入缓存中，这里使用Google的guava缓存，后期使用Redis代替
        TokenCache.set("token_"+username,token);

        return ServerResponse.successRS(token);
    }


    /**
     * 忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @Override
    public ServerResponse<Users> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (username == null || username.equals("")){
            return ServerResponse.defeatedRs("用户名不能为空");
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ServerResponse.defeatedRs("新密码不能为空");
        }
        if (forgetToken == null || forgetToken.equals("")){
            return ServerResponse.defeatedRs("非法的token参数");
        }

        //判断缓存中的token
        String s = TokenCache.get("token_" + username);

        if (s == null || s.equals("")){
            return ServerResponse.defeatedRs("token过期了");
        }
        if (!s.equals(forgetToken)){
            return ServerResponse.defeatedRs("非法的token");
        }

        int i = usersMapper.updateByUserNameAndPassword(username,MD5Utils.getMD5Code(passwordNew));

        if (i <= 0 ){
            return ServerResponse.defeatedRs("修改密码失败");
        }

        return ServerResponse.successRS("修改密码成功");
    }

    /**
     * 登录状态中重置密码
     * @param users
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @Override
    public ServerResponse<Users> resetPassword(Users users, String passwordOld, String passwordNew) {
        if (passwordOld == null || passwordOld.equals("")){
            return ServerResponse.defeatedRs("参数不能为空");
        }
        if (passwordNew == null || passwordNew.equals("")){
            return ServerResponse.defeatedRs("参数不能为空");
        }
        int i = usersMapper.selectByIdAndPassword(users.getId(), MD5Utils.getMD5Code(passwordOld));
        if (i <= 0){
            return ServerResponse.defeatedRs("旧密码输入错误");
        }
        int i1 = usersMapper.updateByUserNameAndPassword(users.getUsername(), MD5Utils.getMD5Code(passwordNew));

        if (i1 <= 0){
            return ServerResponse.defeatedRs("修改密码失败");
        }
        return ServerResponse.successRS("修改密码成功");
    }


}
