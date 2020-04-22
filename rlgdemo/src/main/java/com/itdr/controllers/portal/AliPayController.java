package com.itdr.controllers.portal;


import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.itdr.common.Const;
import com.itdr.common.ServerResponse;
import com.itdr.pojo.Users;
import com.itdr.pojo.pay.Configs;
import com.itdr.services.AliPayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/pay/")
public class AliPayController {

    //日志有关


    @Autowired
    AliPayService aliPayService;

    /**
     * 订单支付
     * @param orderNo
     * @param session
     * @return
     */
    @RequestMapping("pay.do")
    public ServerResponse pay(Long orderNo , HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);

        if (users == null ){
            return ServerResponse.defeatedRs("未登录");
        }

        return aliPayService.pay(orderNo,users.getId());

    }


    /**
     *  支付宝回调
     * @param request
     * @return
     */
    @RequestMapping("alipay_callback.do")
    public String alipayCallback(HttpServletRequest request ,HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);


        //1.获取支付宝返回的参数，返回一个Map集合
        Map<String ,String[]>  map = request.getParameterMap();

        //2.获取该集合的键的set集合
        Set<String> set = map.keySet();
        //3.获取set集合的迭代器
        Iterator<String> iterator = set.iterator();

        //7.创建一个接收参数的集合
        Map<String,String> newmap = new HashMap<>();

        //4.遍历迭代器，重新组装参数
        while (iterator.hasNext()){
            //5.根据key获取map集合中的值
            String key = iterator.next();
            String[] strings = map.get(key);
            //6.遍历值的数组，重新拼装数据
            StringBuffer values = new StringBuffer("");
            for (int i = 0; i < strings.length; i++) {
                values = (i == strings.length-1)? values.append(strings[i]):values.append(strings[i]+",");
            }
            //8.把新的数据以键值对的方式放入一个新的集合中
            newmap.put(key,values.toString());
        }
        //9.取出不必要的参数
        newmap.remove("sign_type");


        try {
            //10.调用支付宝封装的方法进行验签操作，需要（返回数据 + 公钥 + 编码集 + 类型定义
            boolean result = AlipaySignature.rsaCheckV2(newmap, Configs.getAlipayPublicKey(), "UTF-8", Configs.getSignType());

            //判断验签是否通过
            if (! result){
                return "{'msg':'验签失败'}";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "{'msg':'验签失败'}";
        }

        //验签通过去业务层执行业务
        newmap.put("uid", users.getId().toString());
         ServerResponse sr = aliPayService.alipayCallback(newmap);

        //业务层处理完，返回对应的状态信息，这个信息是直接返回给支付宝服务器的，所以必须严格要求准确
        if (sr.isSuccess()){
            return "SUCCESS";
        }else {
            return "FAILLED";
        }

    }

    /**
     * 查询订单支付状态
     * @param orderno
     * @param session
     * @return
     */
    @RequestMapping("query_order_pay_status.do")
    public ServerResponse queryOrderPayStatus(Long orderno , HttpSession session){
        Users users = (Users) session.getAttribute(Const.LOGINUSER);
        ServerResponse sr = aliPayService.queryOrderPayStatus(orderno,users.getId());
        return sr;
    }



}
