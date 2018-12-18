package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmAsyncServiceAPI {


    //获取影片描述信息
    FilmDescVo getFilmDescInfo(String filmId);

    //获取图片信息
    ImgVo getImgInfo(String filmId);

    //获取导演信息
    ActorVo getDirectorInfo(String filmId);

    //获取演员信息
    List<ActorVo> getActors(String filmId);






}
