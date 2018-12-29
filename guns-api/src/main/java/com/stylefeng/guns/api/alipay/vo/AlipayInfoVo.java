package com.stylefeng.guns.api.alipay.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AlipayInfoVo implements Serializable {

   private String orderId;//订单号
   private String QRCodeAddress;//二维码地址

}
