package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.BannerVo;
import com.stylefeng.guns.api.film.vo.FilmInfoVo;
import com.stylefeng.guns.api.film.vo.FilmVo;

import java.util.List;

public interface FilmServiceAPI {

    //获取Banners
    List<BannerVo> getBanners();

    //获取热映影片
    FilmVo getHotFilms(boolean isLimit, int nums);//当isLimitwei为true代表首页的热映模块，为false是电影列表页，nums指的是热映影片数

    //获取即将上映影片（按受欢迎程度排序）
    FilmVo getSoonFilms(boolean isLimit, int nums);//当isLimitweitrue，nums指的是即将上映影片数

    //获取票房排行榜
    List<FilmInfoVo> getBoxRanking();

    //获取人气排行榜
    List<FilmInfoVo> getExpectRanking();

    //获取TOP100
    List<FilmInfoVo> getTop100Films();





}
