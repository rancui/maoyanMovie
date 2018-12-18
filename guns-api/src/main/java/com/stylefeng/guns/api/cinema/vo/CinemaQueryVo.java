package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 影院列表-顶部Tab条件筛选
 */
@Data
public class CinemaQueryVo implements Serializable {

    private Integer brandId=99;
    private Integer hallType=99;
    private Integer areaId=99;
    private Integer pageSize=10;
    private Integer pageNum=1;


}
