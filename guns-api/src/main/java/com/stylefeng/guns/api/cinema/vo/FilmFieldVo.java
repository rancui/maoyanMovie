package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 某场次放映信息
 */
@Data
public class FilmFieldVo implements Serializable {

    private Integer fieldId;
    private String beginTime;
    private String endTime;
    private String language;
    private String hallName;
    private String price;

}
