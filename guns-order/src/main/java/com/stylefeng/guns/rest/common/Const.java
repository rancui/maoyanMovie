package com.stylefeng.guns.rest.common;

public class Const {

    //订单状态
    public enum OrderStatusEnum{
        NO_PAY(0,"未支付"),
        PAY_SUCCESS(1,"支付成功"),
        PAY_Fail(2,"支付失败");
        private String desc;
        private int code;
        public String getDesc() {
            return desc;
        }

        public int getCode() {
            return code;
        }

        OrderStatusEnum(int code,String desc){
            this.code = code;
            this.desc = desc;
        }
        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("么有找到对应的枚举");
        }

    }
}
