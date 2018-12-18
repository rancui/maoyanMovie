package com.stylefeng.guns.rest.modular.controller.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.CinemaQueryVo;
import com.stylefeng.guns.rest.common.ServerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    @Reference(interfaceClass = CinemaServiceAPI.class)
    private CinemaServiceAPI cinemaServiceAPI;


    /**
     * 查询影院列表-根据条件查询所有影院
     * @param cinemaQueryVo
     * @return
     */
    @GetMapping("get_cinemas")
    public ServerResponse getCinemas(CinemaQueryVo cinemaQueryVo){
        //根据入参进行筛选影院
        //
        //
        return null;
    }


    /**
     * 获取影列表查询条件,就是影院列表上的tab筛选
     * @param cinemaQueryVo
     * @return
     */
    @GetMapping("get_condition")
    public  ServerResponse getCondition(CinemaQueryVo cinemaQueryVo){
      return null;
    }

    /**
     * 获取播放场次
     * @param cinemaId
     * @return
     */
    @PostMapping("get_fields")
    public  ServerResponse getFields(Integer cinemaId){
        return null;
    }

    /**
     * 获取场次详细信息
     * @param cinemaId
     * @return
     */
    @PostMapping("get_field_info")
    public  ServerResponse getFieldInfo(Integer cinemaId,Integer fieldId){
        return null;
    }







}
