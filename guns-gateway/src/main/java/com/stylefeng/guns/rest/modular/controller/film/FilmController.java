package com.stylefeng.guns.rest.modular.controller.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.rest.common.ServerResponse;
import com.stylefeng.guns.api.film.vo.FilmIndexVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/film/")
public class FilmController {

    private static final String IMG_PRE = "http://www.rc.com/";

    @Reference(interfaceClass = FilmServiceAPI.class )
    private FilmServiceAPI filmServiceAPI;

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
        filmIndexVo.setHotFilms(filmServiceAPI.getHotFilms(true,8));
        //即将上映的电影
        filmIndexVo.setSoonFilms(filmServiceAPI.getSoonFilms(true,10));
        //票房排行榜
        filmIndexVo.setBoxRanking(filmServiceAPI.getBoxRanking());
        //获取受欢迎榜单
        filmIndexVo.setExpectRanking(filmServiceAPI.getExpectRanking());
        //获取前100名电影
        filmIndexVo.setTop100Films(filmServiceAPI.getTop100Films());

        return ServerResponse.createSuccessImgPreData(IMG_PRE,null,filmIndexVo);
    }

}
