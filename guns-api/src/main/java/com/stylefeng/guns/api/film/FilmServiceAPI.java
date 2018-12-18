package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmServiceAPI {

    //获取Banners
    List<BannerVo> getBanners();

    //获取热映影片，当isLimitwei为true代表首页的热映模块，为false是电影列表页，nums指的是热映影片数
    FilmVo  getHotFilms(boolean isLimit, int pageSize,int pageNum,int sortId,int sourceId,int yearId,int categoryId);

    //获取即将上映影片（按受欢迎程度排序）
    FilmVo getSoonFilms(boolean isLimit, int pageSize,int pageNum,int sortId,int sourceId,int yearId,int categoryId);

    //获取经典影片
    FilmVo  getClassicFilms(boolean isLimit, int pageSize,int pageNum,int sortId,int sourceId,int yearId,int categoryId);

    //获取票房排行榜
    List<FilmInfoVo> getBoxRanking();

    //获取人气排行榜
    List<FilmInfoVo> getExpectRanking();

    //获取TOP100
    List<FilmInfoVo> getTop100Films();

    //============获取影片条件接口

    //分类条件
    List<CategoryVo> getCategories();

    //片源条件
    List<SourceVo> getSources();

    //年代条件
    List<YearVo> getYears();

    //根据影片ID或者名称获取影片信息
    FilmDetailVo getFilmDetail(int searchType,String searchParam);

    //获取影片描述信息
    FilmDescVo getFilmDescInfo(String filmId);

    //获取图片信息
    ImgVo getImgInfo(String filmId);

    //获取导演信息
    ActorVo getDirectorInfo(String filmId);

    //获取演员信息
    List<ActorVo> getActors(String filmId);






}
