package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.util.List;

/**
 * 电影首页
 */
@Data
public class FilmIndexVo {
    private List<BannerVo> bannerVoList;
    private FilmVo hotFilms;//热映电影
    private FilmVo soonFilms;//即将上映的电影
    private List<FilmInfoVo> boxRanking;//票房排行榜
    private List<FilmInfoVo> expectRanking;//受欢迎榜单
    private List<FilmInfoVo> top100Films;//top100的电影



}
