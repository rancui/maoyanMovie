package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmInfoVo implements Serializable {

    private Integer filmId;
    private String filmName;
    private String filmLength;
    private String filmType;
    private String filmCategory;
    private String actors;
    private String imgAddress;
    private List<FilmFieldVo> filmFields;



}
