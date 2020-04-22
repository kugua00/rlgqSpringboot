package com.itdr.controllers.portal;


import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.pojo.vo.CartVO;
import com.itdr.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/portal/cart/")
public class CartController {

    @Autowired
    CartService cartService;


    /**
     * 购物车添加商品
     * @param productId
     * @param count
     * @return
     */
    @RequestMapping("add.do")
    public ServerResponse<CartVO> addOne(Integer productId, Integer count, HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.addOne(productId,count,users.getId());

        }
    }


    /**
     * 获取登录用户购物车列表
     * @param session
     * @return
     */
    @RequestMapping("list.do")
    public ServerResponse<CartVO> listCart(HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.listCart(users.getId());
        }
    }


    /**
     * 更新购物车某个产品数量
     * @param productId
     * @param count
     * @param session
     * @return
     */
    @RequestMapping("update.do")
    public ServerResponse<CartVO> updateCart(Integer productId, Integer count, HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.updateCart(productId,count,users.getId());
        }
    }

    /**
     * 移除购物车某个产品
     * @param productIds
     * @param session
     * @return
     */
    @RequestMapping("delete_cart.do")
    public ServerResponse<CartVO> deleteCart(String productIds, HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.deleteCart(productIds,users.getId());
        }
    }


    /**
     *  查询购物车里的商品信息条数
     * @param session
     * @return
     */
    @RequestMapping("get_cart_product_count.do")
    public ServerResponse<Integer> getCartProductCount(HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.getCartProductCount(users.getId());
        }
    }


    /**
     * 购物车全选
     * @param session
     * @return
     */
    @RequestMapping("select_all.do")
    public ServerResponse<CartVO> selectAll(HttpSession session, Integer check){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.selectAll(users.getId(),check,null);
        }
    }


    /**
     * 购物车取消全选
     * @param session
     * @param check
     * @return
     */
    @RequestMapping("un_select_all.do")
    public ServerResponse<CartVO> unSelectAll(HttpSession session, Integer check){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.selectAll(users.getId(),check,null);
        }
    }

    /**
     * 购物车选中某个商品
     * @param session
     * @param check
     * @return
     */
    @RequestMapping("select.do")
    public ServerResponse<CartVO> select(HttpSession session, Integer check, Integer productId){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.selectAll(users.getId(),check,productId);
        }
    }

    /**
     * 购物车取消选中某个商品
     * @param session
     * @param check
     * @param productId
     * @return
     */
    @RequestMapping("un_select.do")
    public ServerResponse<CartVO> unSelect(HttpSession session, Integer check, Integer productId){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        if (users == null){
            return ServerResponse.defeatedRs(Const.UsersEnum.NO_LOGIN.getCode(),
                    Const.UsersEnum.NO_LOGIN.getDesc());
        }else {
            return cartService.selectAll(users.getId(),check,productId);
        }
    }



}
