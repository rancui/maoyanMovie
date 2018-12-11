package com.stylefeng.guns.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.stylefeng.guns.rest.common.Const;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(String msg, T data) {
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //返回成功
    public static <T> ServerResponse<T> createSuccess() {
        return new ServerResponse(Const.ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse<T> createSuccessMsg(String msg){
        return new ServerResponse(Const.ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createSuccessData(T data){
        return new ServerResponse(Const.ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createSuccessMsgData(String msg,T data){
        return new ServerResponse(Const.ResponseCode.SUCCESS.getCode(),msg,data);
    }

    //返回失败
    public static <T> ServerResponse<T> createError(){
        return new ServerResponse(Const.ResponseCode.ERROR.getCode());
    }
    public static <T> ServerResponse<T> createErrorMsg(String msg){
        return new ServerResponse(Const.ResponseCode.ERROR.getCode(),msg);
    }
    public static <T> ServerResponse<T> createErrorStatusMsg(int status,String msg){
        return new ServerResponse(status,msg);
    }
    public static <T> ServerResponse<T> createByErrorData(T data){
        return  new ServerResponse(Const.ResponseCode.ERROR.getCode(),data);
    }
    public static <T> ServerResponse<T> createErrorMsg(String msg,T data){
        return new ServerResponse(Const.ResponseCode.ERROR.getCode(),msg,data);
    }


    @JsonIgnore
    public  boolean isSuccess(){
        return this.status == Const.ResponseCode.SUCCESS.getCode();
    }


}
