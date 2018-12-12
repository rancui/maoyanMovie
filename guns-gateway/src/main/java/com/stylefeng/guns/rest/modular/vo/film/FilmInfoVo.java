package com.stylefeng.guns.rest.modular.vo.film;

import lombok.Data;

@Data
public class FilmInfoVo {
    private String filmId;
    private int filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;//影片评分
    private int expectNum;//受欢迎人数
    private String showTime;//放映时间
    private int boxNum;//影片票房
    private String score;//电影分数（top100中的电影评分）

}
