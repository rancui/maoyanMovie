package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class AreaVo implements Serializable {

    private Integer areaId;
    private String areaName;
    private boolean isActive;


}
