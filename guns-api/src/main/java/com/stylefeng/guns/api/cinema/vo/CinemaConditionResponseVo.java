package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CinemaConditionResponseVo implements Serializable {
    List<BrandVo> brandVoList;
    List<AreaVo> areaVoList;
    List<HallTypeVo> hallTypeVoList;
}
