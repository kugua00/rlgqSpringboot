package com.itdr.common;

public class Const {


    //用户相关状态
    public static final String LOGINUSER = "login_user";
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";
    public static final String AUTOLOGINTOKEN = "autoLoginToken";
    public static final String JESSESSIONID_COOKIE = "JESSIONID_COOKIE";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    //成功时通用状态码
    public static final int SUCESS = 200;


    //失败时通用状态码
    public static final int ERROR = 100;



    public interface Cart {
        String LIMITQUANTITYSUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMITQUANTITYFAILED = "LIMIT_NUM_FAILED";
        Integer CHECK = 1;
        Integer UNCHECK = 0;
    }





    public enum UsersEnum {
        NEED_LOGIN(2, "需要登录"),
        NO_LOGIN(101, "用户未登录");


        //状态信息
        private int code;
        private String desc;

        private UsersEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    public enum RoleEnum {
        ROLE_ADMIN(0, "管理员"),
        ROLE_CUSTOMER(1, "普通用户");


        //状态信息
        private int code;
        private String desc;

        private RoleEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


    /**
     * 支付枚举
     */
    public enum PaymentPlatformEnum {

        ALIPAY(1, "支付宝"),
        ALIPAY_FALSE(301, "支付宝预下单失败"),
        VERIFY_SIGNATURE_FALSE(302, "支付宝验签失败"),
        VERIFY_ORDER_FALSE(303, "不是本商品的订单"),
        REPEAT_USEALIPAY(304, "支付宝重复调用"),
        SAVEPAYMSG_FALSE(305, "支付信息保存失败");
        private int code;
        private String desc;

        private PaymentPlatformEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }


    }
}