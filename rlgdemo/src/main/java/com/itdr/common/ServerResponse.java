package com.itdr.common;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private Integer status;
    private T data;
    private  String msg;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    /**
     * 获取成功的状态码
     */
    private ServerResponse(){
        this.status = 200;
    }



    /**
     * 获取成功的对象,成功的数据
     */
    private ServerResponse(T data){
        this.status = 200;
        this.data = data;
    }


    /**
     * 获取成功的对象,包括成功的状态码和数据
     */
    private ServerResponse(Integer status, T data){
        this.status = status;
        this.data = data;
    }


    /**
     * 获取成功的对象，包括成功的状态码.数据.状态信息
     */
    private ServerResponse(Integer status, T data,String msg){
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    /**
     * 获取成功的对象，包括成功的状态码.状态信息
     */
    private ServerResponse(Integer status,String msg){
        this.status = status;
        this.msg = msg;
    }



    /**
     * 获取失败的信息
     */
    private ServerResponse(String msg){
        this.msg = msg;
    }


    /**
     * 成功的时候传入数据
     * @param <T>
     * @return
     */
    public static <T> ServerResponse successRS(){
        return new ServerResponse(Const.SUCESS);
    }



    /**
     * 成功的时候传入数据
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ServerResponse successRS(T data){
        return new ServerResponse(Const.SUCESS,data);
    }


    /**
     * 成功的时候只传入状态码.数据
     * @param <T>
     * @return
     */
    public static <T> ServerResponse successRS(String msg){
        return new ServerResponse(Const.SUCESS,msg);
    }

    /**
     * 成功的时候只传入状态码.数据.信息
     * @param data
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ServerResponse successRS(T data, String msg){
        return new ServerResponse(Const.SUCESS,data,msg);
    }



    /**
     * 失败的时候传入失败的信息
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ServerResponse defeatedRs(String msg){
        return new ServerResponse(Const.ERROR,msg);

    }


    /**
     * 失败的时候传入失败的状态码.信息
     * @param status
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ServerResponse defeatedRs(Integer status, String msg){
        return new ServerResponse(status,msg);
    }


    /**
     * 判断是否是成功的方法
     * @return
     */
    @JsonIgnore
     public boolean isSuccess(){
        return this.status == Const.SUCESS;
     }
}
