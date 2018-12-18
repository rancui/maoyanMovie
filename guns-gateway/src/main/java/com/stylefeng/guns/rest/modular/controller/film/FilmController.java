package com.stylefeng.guns.rest.modular.controller.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.google.common.collect.Lists;
import com.stylefeng.guns.api.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.common.ServerResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/film/")
public class FilmController {

    private static final String IMG_PRE = "http://www.rc.com/";

    @Reference(interfaceClass = FilmServiceAPI.class )
    private FilmServiceAPI filmServiceAPI;
    @Reference(interfaceClass = FilmAsyncServiceAPI.class,async = true)
    private FilmAsyncServiceAPI filmAsyncServiceAPI;


    /**
     * API网关：
     *   1.功能聚合【API聚会】
     *   好处：
     *     1.六个接口，一次请求，同一时刻节省了5次http请求
     *     2.同一个接口对外暴露，降低了前后端分离开发的难度和复杂度
     *
     *   坏处：
     *     1.一次性获取数据过多
     *
     * @return
     */

    //首页
    @GetMapping("get_index")
    public ServerResponse<FilmIndexVo> getIndex(){

        FilmIndexVo filmIndexVo = new FilmIndexVo();

        //获取banners
        filmIndexVo.setBannerVoList(filmServiceAPI.getBanners());
        //获取正在热映的电影
        filmIndexVo.setHotFilms(filmServiceAPI.getHotFilms(true,8,1,1,99,99,99));
        //即将上映的电影
        filmIndexVo.setSoonFilms(filmServiceAPI.getSoonFilms(true,8,1,1,99,99,99));
        //票房排行榜
        filmIndexVo.setBoxRanking(filmServiceAPI.getBoxRanking());
        //获取受欢迎榜单
        filmIndexVo.setExpectRanking(filmServiceAPI.getExpectRanking());
        //获取前100名电影
        filmIndexVo.setTop100Films(filmServiceAPI.getTop100Films());

        return ServerResponse.createSuccessImgPreData(IMG_PRE,null,filmIndexVo);
    }

    /**
     * 影片条件列表查询
     * @param categoryId 影片类型编号
     * @param sourceId 片源编号
     * @param yearId 年代编号
     * @return
     */

    @GetMapping("get_condition_film_list")
    public ServerResponse<FilmConditionVo> getMovieList(@RequestParam(value = "categoryId",defaultValue = "99") String categoryId,
                                       @RequestParam(value = "sourceId",defaultValue = "99") String sourceId,
                                       @RequestParam(value = "yearId",defaultValue = "99") String yearId){


        FilmConditionVo filmConditionVo = new FilmConditionVo();

        // 标识位
        boolean flag = false;
        // 类型集合
        List<CategoryVo> cats = filmServiceAPI.getCategories();
        List<CategoryVo> catResult = Lists.newArrayList();
        CategoryVo cat = null;
        for(CategoryVo catVO : cats){
            // 判断集合是否存在catId，如果存在，则将对应的实体变成active状态
            // 6
            // 1,2,3,99,4,5 ->
            /*
                优化：【理论上】
                    1、数据层查询按Id进行排序【有序集合 -> 有序数组】
                    2、通过二分法查找
             */
            if(catVO.getCategoryId().equals(99)){
                cat = catVO;
                continue;
            }
            if(catVO.getCategoryId().equals(categoryId)){
                flag = true;
                catVO.setActive(true);
            }else{
                catVO.setActive(false);
            }
            catResult.add(catVO);
        }
        // 如果不存在，则默认将全部变为Active状态
        if(!flag){
            cat.setActive(true);
            catResult.add(cat);
        }else{
            cat.setActive(false);
            catResult.add(cat);
        }


        // 片源集合
        flag=false;
        List<SourceVo> sources = filmServiceAPI.getSources();
        List<SourceVo> sourceResult = Lists.newArrayList();
        SourceVo sourceVO = null;
        for(SourceVo source : sources){
            if(source.getSourceId().equals(99)){
                sourceVO = source;
                continue;
            }
            if(source.getSourceId().equals(sourceId)){
                flag = true;
                source.setActive(true);
            }else{
                source.setActive(false);
            }
            sourceResult.add(source);
        }
        // 如果不存在，则默认将全部变为Active状态
        if(!flag){
            sourceVO.setActive(true);
            sourceResult.add(sourceVO);
        }else{
            sourceVO.setActive(false);
            sourceResult.add(sourceVO);
        }

        // 年代集合
        flag=false;
        List<YearVo> years = filmServiceAPI.getYears();
        List<YearVo> yearResult = Lists.newArrayList();
        YearVo yearVO = null;
        for(YearVo year : years){
            if(year.getYearId().equals(99)){
                yearVO = year;
                continue;
            }
            if(year.getYearId().equals(yearId)){
                flag = true;
                year.setActive(true);
            }else{
                year.setActive(false);
            }
            yearResult.add(year);
        }
        // 如果不存在，则默认将全部变为Active状态
        if(!flag){
            yearVO.setActive(true);
            yearResult.add(yearVO);
        }else{
            yearVO.setActive(false);
            yearResult.add(yearVO);
        }

        filmConditionVo.setCategoryVoList(catResult);
        filmConditionVo.setSourceVoList(sourceResult);
        filmConditionVo.setYearVoList(yearResult);

        return ServerResponse.createSuccessData(filmConditionVo);
    }



    @GetMapping("film_list")
    public ServerResponse getFilmList(FilmListVo filmListVo){

        FilmVo filmVo = new FilmVo();

        //根据showType判断影片查询类型 1:正在热映 2：即将上映 3：经典影片
           switch (filmListVo.getShowType()){
               case 1:
                   filmVo = filmServiceAPI.getHotFilms(false,filmListVo.getPageSize(),filmListVo.getPageNum(),filmListVo.getSortId(),filmListVo.getSourceId(),filmListVo.getYearId(),filmListVo.getCategoryId());
                   break;
               case 2:
                   filmVo = filmServiceAPI.getSoonFilms(false,filmListVo.getPageSize(),filmListVo.getPageNum(),filmListVo.getSortId(),filmListVo.getSourceId(),filmListVo.getYearId(),filmListVo.getCategoryId());
                   break;
               case 3:
                   filmVo = filmServiceAPI.getClassicFilms(false,filmListVo.getPageSize(),filmListVo.getPageNum(),filmListVo.getSortId(),filmListVo.getSourceId(),filmListVo.getYearId(),filmListVo.getCategoryId());
               default:
                   filmVo = filmServiceAPI.getHotFilms(true,filmListVo.getPageSize(),filmListVo.getPageNum(),filmListVo.getSortId(),filmListVo.getSourceId(),filmListVo.getYearId(),filmListVo.getCategoryId());
                   break;
           }

        return ServerResponse.creatSuccessPageInfo(filmVo.getPageNum(),filmVo.getTotalPage(),IMG_PRE,filmVo.getFilmInfoVoList());

    }

    /**
     * 影片详情查询
     * @param searchParam
     * @param searchType
     * @return
     */
    @GetMapping("query_film_detail/{searchParam}")
    public ServerResponse queryFilmDetail(@PathVariable("searchParam") String searchParam, int searchType) throws ExecutionException, InterruptedException {

        //根据searchType，判断查询类型
        FilmDetailVo filmDetailVo =  filmServiceAPI.getFilmDetail(searchType,searchParam);
        if(filmDetailVo==null){
            return ServerResponse.createErrorMsg("没有可查询的影片");
        }else if (filmDetailVo.getFilmId()==null|| filmDetailVo.getFilmId().trim().length()==0){
            return ServerResponse.createErrorMsg("没有可查询的影片");
        }
        String filmId = filmDetailVo.getFilmId();

        //获取影片描述信息（Dubbo的异步获取）
         filmAsyncServiceAPI.getFilmDescInfo(filmId);
        Future<FilmDescVo> filmDescVoFuture = RpcContext.getContext().getFuture();

        //获取图片信息（Dubbo的异步获取）
        filmAsyncServiceAPI.getImgInfo(filmId);
        Future<ImgVo> imgVoFuture = RpcContext.getContext().getFuture();

        //获取导演信息（Dubbo的异步获取）
        filmAsyncServiceAPI.getDirectorInfo(filmId);
        Future<ActorVo> directorFuture = RpcContext.getContext().getFuture();

        //获取演员信息（Dubbo的异步获取）
        filmAsyncServiceAPI.getActors(filmId);
        Future<List<ActorVo>> actorVoFuture = RpcContext.getContext().getFuture();



        //组织ActorRequestVO （info04字段内的内容）
        ActorRequestVo actorRequestVO = new ActorRequestVo();
        actorRequestVO.setActors(actorVoFuture.get());
        actorRequestVO.setDirector(directorFuture.get());

        //组织InfoRequstVO （应答报文中的info04字段）
        InfoRequestVo infoRequestVo = new InfoRequestVo();
        infoRequestVo.setActors(actorRequestVO);
        infoRequestVo.setBiography(filmDescVoFuture.get().getBiography());
        infoRequestVo.setFilmId(filmId);
        infoRequestVo.setImgVO(imgVoFuture.get());

        filmDetailVo.setInfo04(infoRequestVo);

        return ServerResponse.createSuccessImgPreData(IMG_PRE,null,filmDetailVo);

    }












}
