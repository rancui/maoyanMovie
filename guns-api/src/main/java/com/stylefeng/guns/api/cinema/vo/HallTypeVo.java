package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallTypeVo implements Serializable {

    private Integer hallTypeId;
    private Integer hallTypeName;
    private boolean isActive;


}
