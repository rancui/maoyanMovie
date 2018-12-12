package com.stylefeng.guns.rest.modular.controller.film;

import com.stylefeng.guns.rest.common.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/film/")
public class FilmController {


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
    @GetMapping("get_index")
    public ServerResponse getIndex(){

        //获取banner
        //获取正在热映的电影
        //即将上映的电影
        //票房排行榜
        //获取受欢迎榜单
        //获取前100名电影
        return null;
    }

}
