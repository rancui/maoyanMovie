package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FilmVo implements Serializable {
    private int filmNum;//电影总数
    private int pageNum;//当前页
    private int totalPage;//总页数
    private List<FilmInfoVo>  filmInfoVoList;//电影列表


}
