package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaFieldResponseVo implements Serializable {
    private CinemaInfoVo cinemaInfoVo;
    private FilmInfoVo filmInfoVo;
    private HallInfoVo hallInfoVo;
}
