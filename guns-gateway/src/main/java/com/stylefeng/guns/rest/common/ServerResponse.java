package com.stylefeng.guns.rest.common;

import lombok.Data;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL )
@Data
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;
    private String imgPre;


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
    private ServerResponse(int status, String imgPre,String msg, T data) {
        this.status = status;
        this.imgPre = imgPre;
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
    public static <T> ServerResponse<T> createSuccessMsgData(String msg, T data){
        return new ServerResponse(Const.ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public static <T> ServerResponse<T> createSuccessImgPreData(String imgPre, String msg, T data){
        return new ServerResponse(Const.ResponseCode.SUCCESS.getCode(),imgPre,msg,data);
    }

    //返回失败
    public static <T> ServerResponse<T> createError(){
        return new ServerResponse(Const.ResponseCode.ERROR.getCode());
    }

    public static <T> ServerResponse<T> createErrorMsg(String msg){
        return new ServerResponse(Const.ResponseCode.ERROR.getCode(),msg);
    }
    public static <T> ServerResponse<T> createErrorStatusMsg(int status, String msg){
        return new ServerResponse(status,msg);
    }
    public static <T> ServerResponse<T> createByErrorData(T data){
        return  new ServerResponse(Const.ResponseCode.ERROR.getCode(),data);
    }
    public static <T> ServerResponse<T> createErrorMsgData(String msg, T data){
        return new ServerResponse(Const.ResponseCode.ERROR.getCode(),msg,data);
    }
    public static <T> ServerResponse<T> createErrorImgPreData(String imgPre, String msg,T data){
        return new ServerResponse(Const.ResponseCode.ERROR.getCode(),imgPre,msg,data);
    }




}
