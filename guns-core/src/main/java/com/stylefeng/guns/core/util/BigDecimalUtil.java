package com.stylefeng.guns.core.util;

/**
 * 加减乘除法
 */

import java.math.BigDecimal;

public class BigDecimalUtil {


    private BigDecimalUtil(){

    }

    /**
     * 加法
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(double v1,double v2){
        BigDecimal a = new BigDecimal(Double.toString(v1));
        BigDecimal b = new BigDecimal(Double.toString(v2));
        return a.add(b);
    }
    /**
     * 减法
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal subtract(double v1,double v2){
        BigDecimal a = new BigDecimal(Double.toString(v1));
        BigDecimal b = new BigDecimal(Double.toString(v2));
        return a.subtract(b);
    }
    /**
     * 乘法
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal multiply(double v1,double v2){
        BigDecimal a = new BigDecimal(Double.toString(v1));
        BigDecimal b = new BigDecimal(Double.toString(v2));
        return a.multiply(b);
    }
    /**
     * 除法
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal divide(double v1,double v2){
        BigDecimal a = new BigDecimal(Double.toString(v1));
        BigDecimal b = new BigDecimal(Double.toString(v2));
        //四舍五入,保留2位小数
        return a.divide(b,2,BigDecimal.ROUND_HALF_UP);
    }





}
