package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FilmFieldVo implements Serializable {

    private Integer fieldId;
    private Date beginTime;
    private Date endTime;
    private String language;
    private String hallName;
    private String price;

}
