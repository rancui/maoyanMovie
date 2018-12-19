package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CinemaResponseListVo implements Serializable {

    private List<CinemaVo>  cinemaVoList;


}
