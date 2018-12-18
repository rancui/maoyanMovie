package com.stylefeng.guns.rest.modular.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.stylefeng.guns.api.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(interfaceClass = FilmAsyncServiceAPI.class,async = true)
public class FilmAsyncServiceImpl implements FilmAsyncServiceAPI {

    @Autowired
    private MoocFilmInfoMapper moocFilmInfoMapper;
    @Autowired
    private MoocActorMapper moocActorMapper;





    private MoocFilmInfo getFilmInfo(String filmId){
        MoocFilmInfo moocFilmInfo = new MoocFilmInfo();
        moocFilmInfo.setFilmId(filmId);
        moocFilmInfo = moocFilmInfoMapper.selectOne(moocFilmInfo);

        return moocFilmInfo;
    }


    @Override
    public FilmDescVo getFilmDescInfo(String filmId) {

        MoocFilmInfo moocFilmInfo= getFilmInfo(filmId);

        FilmDescVo filmDescVo = new FilmDescVo();
        filmDescVo.setFilmId(filmId);
        filmDescVo.setBiography(moocFilmInfo.getBiography());
        return filmDescVo;
    }

    @Override
    public ImgVo getImgInfo(String filmId) {
        MoocFilmInfo moocFilmInfo= getFilmInfo(filmId);
        ImgVo imgVo = new ImgVo();
        String[] filmImgs = moocFilmInfo.getFilmImgs().split(",");
        imgVo.setMainImg(filmImgs[0]);
        imgVo.setImg01(filmImgs[1]);
        imgVo.setImg01(filmImgs[2]);
        imgVo.setImg01(filmImgs[3]);
        imgVo.setImg01(filmImgs[4]);
        return imgVo;
    }

    @Override
    public ActorVo getDirectorInfo(String filmId) {
        MoocFilmInfo moocFilmInfo = getFilmInfo(filmId);
        //获取导演编号
        Integer directorId = moocFilmInfo.getDirectorId();
        MoocActor moocActor = moocActorMapper.selectById(directorId);

        ActorVo actorVo = new ActorVo();
        actorVo.setImgAddress(moocActor.getActorImg());
        actorVo.setDirectorName(moocActor.getActorName());

        return actorVo;
    }

    @Override
    public List<ActorVo> getActors(String filmId) {

        return moocActorMapper.getActors(filmId);
    }


}
