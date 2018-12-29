package com.stylefeng.guns.rest.common;

public class Const {

    // 服务端返回的响应状态码
    public enum ResponseCode{
        ERROR(0,"ERROR"),
        SUCCESS(1,"SUCCESS"),
        ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT"),
        NEED_LOGIN(3,"NEED_LOGIN");

        private final  int code;
        private  final  String desc;
        ResponseCode(int code,String desc){
            this.code = code;
            this.desc = desc;
        }
        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

    }

}
