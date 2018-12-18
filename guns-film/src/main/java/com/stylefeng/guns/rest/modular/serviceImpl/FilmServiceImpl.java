package com.stylefeng.guns.rest.modular.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
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
    @Autowired
    private MoocFilmInfoMapper moocFilmInfoMapper;
    @Autowired
    private MoocCategoryDictMapper moocCategoryDictMapper;
    @Autowired
    private MoocSourceDictMapper moocSourceDictMapper;
    @Autowired
    private MoocYearDictMapper moocYearDictMapper;
    @Autowired
    private MoocActorMapper moocActorMapper;


    /**
     * 首页Banner
     * @return
     */
    @Override
    public List<BannerVo> getBanners() {
        List<MoocBanner> bannerList = moocBannerMapper.selectList(null);
        List<BannerVo> bannerVoList = Lists.newArrayList();
        for (MoocBanner moocBanner : bannerList) {
            BannerVo bannerVo = new BannerVo();
            bannerVo.setBannerAddress(moocBanner.getBannerAddress());
            bannerVo.setBannerId(moocBanner.getUuid());
            bannerVo.setBannerUrl(moocBanner.getBannerUrl());
            bannerVoList.add(bannerVo);
        }
        return bannerVoList;
    }

    private List<FilmInfoVo> assembleFilmInfoVo(List<MoocFilm> moocFilmList) {

        List<FilmInfoVo> filmInfoVoList = Lists.newArrayList();

        for (MoocFilm moocFilm : moocFilmList) {
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

        return filmInfoVoList;
    }


    /**
     * 热映影片
     * @param isLimit true为首页，false为其他页
     * @param pageSize
     * @param pageNum
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param categoryId
     * @return
     */
    @Override
    public FilmVo getHotFilms(boolean isLimit, int pageSize,int pageNum,int sortId,int sourceId,int yearId,int categoryId) {
        return assembleFilmCommon(isLimit,pageSize,pageNum,sortId,sourceId,yearId,categoryId,"1");

    }

    /**
     * 即将上映
     * @param isLimit
     * @param pageSize
     * @param pageNum
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param categoryId
     * @return
     */
    @Override
    public FilmVo getSoonFilms(boolean isLimit, int pageSize,int pageNum,int sortId,int sourceId,int yearId,int categoryId) {
        return assembleFilmCommon(isLimit,pageSize,pageNum,sortId,sourceId,yearId,categoryId,"2");

    }

    /**
     * 查询经典电影
     * @param pageSize
     * @param pageNum
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param categoryId
     * @return
     */
   @Override
   public FilmVo getClassicFilms(boolean isLimit, int pageSize,int pageNum,int sortId,int sourceId,int yearId,int categoryId){
       return assembleFilmCommon(isLimit,pageSize,pageNum,sortId,sourceId,yearId,categoryId,"3");
   }

    /**
     * 组装热映电影，即将上映，经典影片的共同部分
     * @param isLimit
     * @param pageSize
     * @param pageNum
     * @param sortId
     * @param sourceId
     * @param yearId
     * @param categoryId
     * @param showType 电影类型
     * @return
     */
    private FilmVo assembleFilmCommon(boolean isLimit,int pageSize,int pageNum,int sortId,int sourceId,int yearId,
                                   int categoryId,String showType ){

        FilmVo filmVO = new FilmVo();
        List<FilmInfoVo> filmInfoVoList = Lists.newArrayList();

        // 即将上映影片的限制条件
        EntityWrapper<MoocFilm> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status",showType);

        //判断是否是首页需要的内容
        if (isLimit) {//如果是，则限制条数，限制内容为热映电影
            Page<MoocFilm> moocFilmPage = new Page<>(1, pageSize);
            List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage, entityWrapper);
            filmInfoVoList = assembleFilmInfoVo(moocFilmList);
            filmVO.setFilmNum(moocFilmList.size());
            filmVO.setFilmInfoVoList(filmInfoVoList);

        } else {// 如果不是，则是列表页，同样需要限制内容为对应类型的上映影片
            Page<MoocFilm> moocFilmPage = null;
            // 排序，根据sortId的不同，来组织不同的Page对象
            // 1:按热门搜索，2:按时间搜索，3:按评价搜索
            switch (sortId){
                case 1 :
                    moocFilmPage = new Page<>(pageNum,pageSize,"film_box_office");
                    break;
                case 2 :
                    moocFilmPage = new Page<>(pageNum,pageSize,"film_time");
                    break;
                case 3 :
                    moocFilmPage = new Page<>(pageNum,pageSize,"film_score");
                    break;
                default:
                    moocFilmPage = new Page<>(pageNum,pageSize,"film_box_office");
                    break;
            }

            //如果categoryId，sourceId，yearId都不为99（全部标签），则表示要按照对应的编号进行查询
            if(sourceId != 99){
                entityWrapper.eq("film_source",sourceId);
            }
            if(yearId != 99){
                entityWrapper.eq("film_date",yearId);
            }
            if(categoryId != 99){
                // #2#4#22#
                String catStr = "%#"+categoryId+"#%";
                entityWrapper.like("film_cats",catStr);
            }

            List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage, entityWrapper);

            //组织filmInfoVoList
            filmInfoVoList = assembleFilmInfoVo(moocFilmList);
            filmVO.setFilmNum(moocFilmList.size());

            // 需要总页数 totalCounts/pageSize -> 0 + 1 = 1
            // 每页10条，我现在有6条 -> 1
            int totalCounts = moocFilmMapper.selectCount(entityWrapper);
            int totalPages = (totalCounts/pageSize)+1;

            filmVO.setFilmInfoVoList(filmInfoVoList);
            filmVO.setPageNum(pageNum);
            filmVO.setTotalPage(totalPages);
        }

        return filmVO;
    }


    //票房排行,前10名
    @Override
    public List<FilmInfoVo> getBoxRanking() {
        //正在上映，票房前10名
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("film_status", "2");

        Page<MoocFilm> moocFilmPage = new Page<>(1, 10, "film_box_office");
        List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage, entityWrapper);

        List<FilmInfoVo> filmInfoVoList = assembleFilmInfoVo(moocFilmList);
        return filmInfoVoList;
    }

    //受欢迎的电影
    @Override
    public List<FilmInfoVo> getExpectRanking() {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("film_status", "2");

        Page<MoocFilm> moocFilmPage = new Page<>(1, 10, "film_preSaleNum");
        List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage, entityWrapper);

        List<FilmInfoVo> filmInfoVoList = assembleFilmInfoVo(moocFilmList);
        return filmInfoVoList;
    }

    //TOP100
    @Override
    public List<FilmInfoVo> getTop100Films() {
        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("film_status", "2");

        Page<MoocFilm> moocFilmPage = new Page<>(1, 10, "film_score");
        List<MoocFilm> moocFilmList = moocFilmMapper.selectPage(moocFilmPage, entityWrapper);

        List<FilmInfoVo> filmInfoVoList = assembleFilmInfoVo(moocFilmList);
        return filmInfoVoList;
    }

    @Override
    public List<CategoryVo> getCategories() {

        List<CategoryVo> categoryVoList = Lists.newArrayList();

        //查询实体对象
        List<MoocCategoryDict> categoryDictList = moocCategoryDictMapper.selectList(null);

        //将实体对象转换成VO对象
        for (MoocCategoryDict categoryDict : categoryDictList) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setCategoryName(categoryDict.getShowName());
            categoryVo.setCategoryId(categoryDict.getUuid());
            categoryVoList.add(categoryVo);
        }
        return categoryVoList;

    }

    @Override
    public List<SourceVo> getSources() {
        List<SourceVo> sourceVoList = Lists.newArrayList();
        //查询实体对象
        List<MoocSourceDict> sourceDictList = moocSourceDictMapper.selectList(null);
        //将实体对象转换成VO对象
        for (MoocSourceDict moocSourceDict : sourceDictList) {
            SourceVo sourceVo = new SourceVo();
            sourceVo.setSourceId(moocSourceDict.getUuid());
            sourceVo.setSourceName(moocSourceDict.getShowName());
            sourceVoList.add(sourceVo);
        }

        return sourceVoList;
    }

    @Override
    public List<YearVo> getYears() {
        List<YearVo> yearVoList = Lists.newArrayList();
        //查询实体对象
        List<MoocYearDict> yearDictList = moocYearDictMapper.selectList(null);
        //将实体对象转换成VO对象
        for (MoocYearDict moocYearDict : yearDictList) {
            YearVo yearVo = new YearVo();
            yearVo.setYearId(moocYearDict.getUuid());
            yearVo.setYearName(moocYearDict.getShowName());
            yearVoList.add(yearVo);
        }

        return yearVoList;
    }

    /**
     * 影片详情查询
     * @param searchType
     * @param searchParam
     * @return
     */
    @Override
    public FilmDetailVo getFilmDetail(int searchType, String searchParam) {

        FilmDetailVo filmDetailVo = null;

        //根据searchType，判断查询类型. 1-按名称 2-按ID
        if(searchType==1){
            filmDetailVo = moocFilmMapper.getFilmDetailByName("%"+searchParam+"%");
        }else {
            filmDetailVo = moocFilmMapper.getFilmDetailByUUId(searchParam);
        }

        return filmDetailVo;
    }


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
