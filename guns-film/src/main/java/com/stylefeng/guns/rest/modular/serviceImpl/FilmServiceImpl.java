package com.stylefeng.guns.rest.modular.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.BannerVo;
import com.stylefeng.guns.api.film.vo.FilmInfoVo;
import com.stylefeng.guns.api.film.vo.FilmVo;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.MoocBannerMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MoocFilmMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocBanner;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilm;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Service(interfaceClass = FilmServiceAPI.class)
public class FilmServiceImpl implements FilmServiceAPI {

    @Autowired
    private MoocBannerMapper moocBannerMapper;
    @Autowired
    private MoocFilmMapper moocFilmMapper;

    //首页Banner
    @Override
    public List<BannerVo> getBanners() {
        List<MoocBanner> bannerList = moocBannerMapper.selectList(null);
        List<BannerVo> bannerVoList = Lists.newArrayList();

        if(CollectionUtils.isNotEmpty(bannerList)){
            for(MoocBanner moocBanner:bannerList){
                BannerVo bannerVo = new BannerVo();
                bannerVo.setBannerAddress(moocBanner.getBannerAddress());
                bannerVo.setBannerId(moocBanner.getUuid());
                bannerVo.setBannerUrl(moocBanner.getBannerUrl());
                bannerVoList.add(bannerVo);
            }
        }

        return bannerVoList;
    }

    private List<FilmInfoVo> assmbleFilmInfoVo(List<MoocFilm> moocFilmList){

        List<FilmInfoVo> filmInfoVoList = Lists.newArrayList();

        if(CollectionUtils.isNotEmpty(moocFilmList)){
            for(MoocFilm moocFilm:moocFilmList){
                FilmInfoVo filmInfoVo = new FilmInfoVo();

                filmInfoVo.setShowTime(DateUtil.getDay(moocFilm.getFilmTime()));
                filmInfoVo.setImgAddress(moocFilm.getImgAddress());
                filmInfoVo.setScore(moocFilm.getFilmScore());
                filmInfoVo.setFilmType(moocFilm.getFilmType());
                filmInfoVo.setFilmName(moocFilm.getFilmName());
                filmInfoVo.setFilmId(moocFilm.getUuid());
                filmInfoVo.setExpectNum(moocFilm.getFilmPresalenum());
                filmInfoVo.setBoxNum(moocFilm.getFilmBoxOffice());
                filmInfoVo.setFilmScore(moocFilm.getFilmScore());
                filmInfoVoList.add(filmInfoVo);
            }
        }

        return filmInfoVoList;
    }


    //热映影片
    @Override
    public FilmVo getHotFilms(boolean isLimit, int nums) {
        FilmVo filmVo = new FilmVo();
        //热映影片的限制条件
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("film_status","1");
        //判断是否是首页需要的内容
        if(isLimit){//如果是，则限制条数，限制内容为热映电影
            Page<MoocFilm> moocFilmPage = new Page<>(1,nums);
            List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage,entityWrapper);

            List<FilmInfoVo> filmInfoVoList = assmbleFilmInfoVo(moocFilmList);

            filmVo.setFilmNum(moocFilmList.size());
            filmVo.setFilmInfoVoList(filmInfoVoList);

        }else {//如果不是，则是列表页，同样需要限制内容为热映影片


        }

        return filmVo;
    }

    //即将上映
    @Override
    public FilmVo getSoonFilms(boolean isLimit, int nums) {
        FilmVo filmVo = new FilmVo();
        //即将上映影片的限制条件
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("film_status","2");
        //判断是否是首页需要的内容
        if(isLimit){//如果是，则限制条数，限制内容为热映电影
            Page<MoocFilm> moocFilmPage = new Page<>(1,nums);
            List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage,entityWrapper);

            List<FilmInfoVo> filmInfoVoList = assmbleFilmInfoVo(moocFilmList);

            filmVo.setFilmNum(moocFilmList.size());
            filmVo.setFilmInfoVoList(filmInfoVoList);


        }else {//如果不是，则是列表页，同样需要限制内容为热映影片


        }

        return filmVo;
    }

    //票房排行,前10名
    @Override
    public List<FilmInfoVo> getBoxRanking() {
        //正在上映，票房前10名
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("film_status","2");

        Page<MoocFilm> moocFilmPage = new Page<>(1,10,"film_box_office");
        List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage,entityWrapper);

        List<FilmInfoVo> filmInfoVoList = assmbleFilmInfoVo(moocFilmList);
        return filmInfoVoList;
    }

    //受欢迎的电影
    @Override
    public List<FilmInfoVo> getExpectRanking() {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("film_status","2");

        Page<MoocFilm> moocFilmPage = new Page<>(1,10,"film_preSaleNum");
        List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage,entityWrapper);

        List<FilmInfoVo> filmInfoVoList = assmbleFilmInfoVo(moocFilmList);
        return filmInfoVoList;
    }

    //TOP100
    @Override
    public List<FilmInfoVo> getTop100Films() {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("film_status","2");

        Page<MoocFilm> moocFilmPage = new Page<>(1,10,"film_score");
        List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage,entityWrapper);

        List<FilmInfoVo> filmInfoVoList = assmbleFilmInfoVo(moocFilmList);
        return filmInfoVoList;
    }

}
