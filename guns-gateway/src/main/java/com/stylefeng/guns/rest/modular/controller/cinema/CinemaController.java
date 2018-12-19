package com.stylefeng.guns.rest.modular.controller.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    public static final String IMG_PRE = "//www.rc.com";

    @Reference(interfaceClass = CinemaServiceAPI.class,connections = 10,cache = "lru")
    private CinemaServiceAPI cinemaServiceAPI;


    /**
     * 查询影院列表-根据条件查询所有影院
     * @param cinemaQueryVo
     * @return
     */
    @RequestMapping("get_cinemas")
    public ServerResponse getCinemas(CinemaQueryVo cinemaQueryVo){
        try {
            //根据入参进行筛选影院
            Page<CinemaVo> cinemaVoPage = cinemaServiceAPI.getCinemas(cinemaQueryVo);
            //判断是否有满足条件的影院
            if(cinemaVoPage.getRecords()==null||cinemaVoPage.getRecords().size()==0){
                return ServerResponse.createErrorMsg("没有满足条件的影院");
            }else {

                return ServerResponse.creatSuccessPageInfo(cinemaVoPage.getCurrent(),(int)cinemaVoPage.getPages(),IMG_PRE,cinemaVoPage.getRecords());
            }
        } catch (Exception e) {
            log.error("获取影院列表失败:{}",e);
            return ServerResponse.createErrorMsg("查询影院列表失败");
        }

    }


    /**
     * 获取影列表查询条件,就是影院列表上的tab筛选
     * @param cinemaQueryVo
     * @return
     */
    @GetMapping("get_condition")
    public  ServerResponse getCondition(CinemaQueryVo cinemaQueryVo){

        try {
            //获取三个集合，然后封装成一个对象返回
            List<BrandVo> brands = cinemaServiceAPI.getBrands(cinemaQueryVo.getBrandId());
            List<AreaVo> areas = cinemaServiceAPI.getAreas(cinemaQueryVo.getAreaId());
            List<HallTypeVo> hallTypes = cinemaServiceAPI.getHallType(cinemaQueryVo.getHallType());

            CinemaConditionResponseVo cinemaConditionResponseVo = new CinemaConditionResponseVo();
            cinemaConditionResponseVo.setAreaVoList(areas);
            cinemaConditionResponseVo.setBrandVoList(brands);
            cinemaConditionResponseVo.setHallTypeVoList(hallTypes);

            return ServerResponse.createSuccessData(cinemaConditionResponseVo);
        } catch (Exception e) {
            log.error("获取影院列表查询条件失败:{}",e);
            return ServerResponse.createErrorMsg("获取影院列表查询条件失败");
        }
    }

    /**
     * 获取播放场次列表
     * @param cinemaId
     * @return
     */
    @GetMapping("get_fields")
    public  ServerResponse getFields(Integer cinemaId){

        try {

           CinemaInfoVo cinemaInfoVo = cinemaServiceAPI.getCinemaInfoById(cinemaId);
           List<FilmInfoVo> filmInfoVoList = cinemaServiceAPI.getFilmInfoById(cinemaId);
           CinemaFieldsResponseVo cinemaFieldResponseVo = new CinemaFieldsResponseVo();
           cinemaFieldResponseVo.setCinemaInfoVo(cinemaInfoVo);
           cinemaFieldResponseVo.setFilmInfoVoList(filmInfoVoList);
           return ServerResponse.createSuccessImgPreData(IMG_PRE,null,cinemaFieldResponseVo);


        }catch (Exception e){
            log.error("获取播放场次失败:{}",e);
            return ServerResponse.createErrorMsg("获取播放场次失败");
        }

    }

    /**
     * 获取某场次详细信息
     * @param cinemaId
     * @return
     */
    @PostMapping("get_field_info")
    public  ServerResponse getFieldInfo(Integer cinemaId,Integer fieldId){
        try {
            FilmInfoVo filmInfoVo = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
            CinemaInfoVo  cinemaInfoVo = cinemaServiceAPI.getCinemaInfoById(cinemaId);
            HallInfoVo hallInfoVo = cinemaServiceAPI.getFilmFieldInfo(fieldId);

            //造几个销售的假数据，后续会对接订单接口
            hallInfoVo.setSoldSeats("1,2,3");

            CinemaFieldResponseVo cinemaFieldResponseVo = new CinemaFieldResponseVo();
            cinemaFieldResponseVo.setCinemaInfoVo(cinemaInfoVo);
            cinemaFieldResponseVo.setFilmInfoVo(filmInfoVo);
            cinemaFieldResponseVo.setHallInfoVo(hallInfoVo);

            return ServerResponse.createSuccessImgPreData(IMG_PRE,null,cinemaFieldResponseVo);

        }catch (Exception e){
            log.error("获取选座细信息失败:{}",e);
            return ServerResponse.createErrorMsg("获取选座细信息失败");
        }
    }



}
