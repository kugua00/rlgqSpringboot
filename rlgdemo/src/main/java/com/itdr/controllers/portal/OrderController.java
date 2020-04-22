package com.itdr.controllers.portal;


import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.services.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/order/")
public class OrderController {

    @Autowired
    OrderService orderService;


    /**
     * 创建订单
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("create.do")
    public ServerResponse createOrder(HttpSession session, Integer shippingId){
        Users u = (Users) session.getAttribute(Const.LOGINUSER);
        if (u == null){
            return ServerResponse.defeatedRs("用户未登录");
        }
        ServerResponse order = orderService.createOrder(u.getId(), shippingId);

        return order;
    }


    /**
     * 获取订单详情信息
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session,
                                              @RequestParam(value = "orderNo",required = false) Long orderNo){
        Users u = (Users) session.getAttribute(Const.LOGINUSER);
        if (u == null){
            return ServerResponse.defeatedRs("用户未登录");
        }
        ServerResponse order = orderService.getOrderCartProduct(u.getId(),orderNo);

        return order;
    }

    /**
     * 获取登录用户的订单列表
     * @param session
     * @param pageSize
     * @param pageNum
     * @return
     */
    @RequestMapping("list.do")
    public ServerResponse listdo(HttpSession session,
                                 @RequestParam(value = "pageSize",required = false, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "pageNum",required = false, defaultValue = "1")Integer pageNum ){
        Users u = (Users) session.getAttribute(Const.LOGINUSER);
        if (u == null){
            return ServerResponse.defeatedRs("用户未登录");
        }
        ServerResponse order = orderService.listdo(u.getId(),pageSize,pageNum);

        return order;
    }


    /**
     * 取消订单
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("countermand_order.do")
    public ServerResponse countermandOrder(HttpSession session, Long orderNo){
        Users u = (Users) session.getAttribute(Const.LOGINUSER);
        if (u == null){
            return ServerResponse.defeatedRs("用户未登录");
        }
        ServerResponse order = orderService.countermandOrder(u.getId(),orderNo);

        return order;
    }


}
