package com.itdr.controllers.portal;


import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Shippings;
import com.itdr.pojo.Users;
import com.itdr.services.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/shipping/")
public class ShippingController {

    @Autowired
    ShippingService shippingService;


    /**
     *查询收货地址
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    public ServerResponse<Shippings> listdo(HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs("用户未登录");
        }
        return shippingService.listdo(users.getId());

    }


    /**
     * 添加收货地址
     * @param shippings
     * @param session
     * @return
     */
    @RequestMapping("insert.do")
    public ServerResponse<Shippings> insertdo(Shippings shippings , HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);

        if (users == null){
            return ServerResponse.defeatedRs("用户未登录");
        }
        shippings.setUserId(users.getId());
        return shippingService.insertdo(shippings);

    }


    /**
     * 删除收货地址
     * @param shippings
     * @param session
     * @return
     */
    @RequestMapping("delect.do")
    public ServerResponse<Shippings> deletedo(Shippings shippings, HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs("用户未登录");
        }
        return shippingService.deletedo(shippings);

    }


    /**
     * 修改收货地址
     * @param session
     * @return
     */
    @RequestMapping("update.do")
    public ServerResponse<Shippings> updatedo(Shippings shippings, HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs("用户未登录");
        }
        return shippingService.updatedo(shippings);

    }

}
