package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlipayResultVo implements Serializable {
    private String orderId;//订单号
    private String orderStatus;//订单状态
    private String orderMsg;
}
