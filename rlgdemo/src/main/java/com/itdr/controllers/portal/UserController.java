package com.itdr.controllers.portal;


import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/user/")
public class UserController {


    @Autowired
    UserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("login.do")
    public ServerResponse<Users> login(String username, String password, HttpSession session){
        ServerResponse<Users> sr = userService.login(username,password);

        //当返回的是成功状态执行
        if (sr.isSuccess()) {

            Users users = sr.getData();
            session.setAttribute(Const.LOGINUSER, users);

            Users u2 = new Users();
            u2.setId(users.getId());
            u2.setUsername(users.getUsername());
            u2.setEmail(users.getEmail());
            u2.setPhone(users.getPhone());
            u2.setCreateTime(users.getCreateTime());
            u2.setUpdateTime(users.getUpdateTime());
            users.setPassword("");

            sr.setData(u2);
        }
        return sr;
    }

    /**
     * 用户注册
     * @param u
     * @return
     */
    @PostMapping("register.do")
    public ServerResponse<Users> register(Users u  ){
        ServerResponse<Users> sr = userService.register(u);
        return sr;
    }

    /**
     * 检查用户是否存在
     * @param str
     * @param type
     * @return
     */
    @PostMapping("check_valid.do")
    public ServerResponse<Users> checkUserName(String str, String type){
        ServerResponse<Users> sr = userService.checkUserName(str,type);
        return sr;
    }


    /**
     * 获取登录用户的信息
     * @param session
     * @return
     */
    @PostMapping("get_user_info.do")
    public ServerResponse<Users> getUserInfo(HttpSession session){
        Users user = (Users) session.getAttribute(Const.LOGINUSER);
        if (user == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return ServerResponse.successRS(user);
        }
    }


    /**
     * 退出登录
     * @param session
     * @return
     */
    @PostMapping("logout.do")
    public ServerResponse<Users> logout(HttpSession session){
        session.removeAttribute(Const.LOGINUSER);
        return ServerResponse.successRS("退出成功");
    }


    /**
     * 获取当前登录用户的详细信息
     * @param session
     * @return
     */
    @PostMapping("get_inforamtion.do")
    public ServerResponse<Users> getInforamtion(HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return  userService.getInforamtion(users);
        }

    }


    /**
     * 登录状态更新个人信息
     * @param user,session
     * @return
     */
    @PostMapping("update_information.do")
    public ServerResponse<Users> updateInformation(Users user, HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(), Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            user.setId(users.getId());
            user.setUsername(users.getUsername());
            ServerResponse<Users> sr = userService.updateInformation(user);
            if (sr.getStatus() == 103){
                session.setAttribute(Const.LOGINUSER,user);
                return  sr;
            }else {
                return sr;
            }
        }

    }


    /**
     * 忘记密码
     * @param username
     * @return
     */
    @PostMapping("forget_get_question.do")
    public ServerResponse<Users> forgetGetQuestion(String username){
        return userService.forgetGetQuestion(username);
    }


    /**
     * 提交问题答案
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @PostMapping("forget_check_answer.do")
    public ServerResponse<Users> forgetCheckAnswer(String username, String question, String answer){
        return userService.forgetCheckAnswer(username,question,answer);
    }


    /**
     * 忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @PostMapping("forget_reset_password.do")
    public ServerResponse<Users> forgetResetPassword(String username, String passwordNew, String forgetToken){
        return userService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登录状态中重置密码
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @PostMapping("reset_password.do")
    public ServerResponse<Users> resetPassword(HttpSession session, String passwordOld, String passwordNew){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs("用户未登录");
        }else {
            return userService.resetPassword(users,passwordOld,passwordNew);
        }
    }

}
