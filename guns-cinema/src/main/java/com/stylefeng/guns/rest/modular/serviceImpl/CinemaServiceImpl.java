package com.stylefeng.guns.rest.modular.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Service(interfaceClass =CinemaServiceAPI.class )
public class CinemaServiceImpl implements CinemaServiceAPI {
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private HallMapper hallMapper;
    @Autowired
    private CinemaMapper cinemaMapper;
    @Autowired
    private HallFilmInfoMapper hallFilmInfoMapper;
    @Autowired
    private BrandMapper brandMapper;


    /**
     * 根据入参查询影院列表
     * @param cinemaQueryVo
     * @return
     */
    public Page<CinemaVo> getCinemas(CinemaQueryVo cinemaQueryVo){

        Page<Cinema> page = new Page<>(cinemaQueryVo.getPageNum(),cinemaQueryVo.getPageSize());
        EntityWrapper<Cinema> entityWrapper = new EntityWrapper<Cinema>();

        if(cinemaQueryVo.getBrandId()!=90){
            entityWrapper.eq("brand_id",cinemaQueryVo.getBrandId());
        }
        if(cinemaQueryVo.getAreaId()!=90){
            entityWrapper.eq("area_id",cinemaQueryVo.getAreaId());
        }
        if(cinemaQueryVo.getBrandId()!=90){
            entityWrapper.like("hall_ids","%#"+cinemaQueryVo.getHallType()+"#%");
        }


        List<Cinema> cinemaList = cinemaMapper.selectPage(page,entityWrapper);

        List<CinemaVo> cinemaVoList = Lists.newArrayList();

        for(Cinema cinema:cinemaList){
              CinemaVo cinemaVo = new CinemaVo();
              cinemaVo.setUuid(cinema.getUuid());
              cinemaVo.setAddress(cinema.getCinemaAddress());
              cinemaVo.setCinemaName(cinema.getCinemaName());
              cinemaVo.setMinPrice(cinema.getMinimumPrice());
              cinemaVoList.add(cinemaVo);

        }

        //判断影院列表总数
        int totalCount = cinemaMapper.selectCount(entityWrapper);
        Page<CinemaVo> result = new Page<>();
        result.setRecords(cinemaVoList);
        result.setSize(cinemaQueryVo.getPageSize());
        result.setTotal(totalCount);

        return result;
    }

    /**
     * 根据条件获取品牌列表【除了99（全部）外，其他的数字为isActive】
     * @param brandId
     * @return
     */
    public List<BrandVo> getBrands(int brandId){
        return null;
    }

    /**
     * 获取地区列表
     * @param areaId
     * @return
     */
    public List<AreaVo> getAreas(int areaId){
        return null;
    }


    /**
     * 获取影厅类型列表
     * @param hallType
     * @return
     */
    public List<HallTypeVo> getHallType(int hallType) {
        return null;
    }


    /**
     * 根据影院编号，获取影院信息
     * @param cinemaId
     * @return
     */
    public CinemaInfoVo getCinemaInfoById(int cinemaId) {
        return null;
    }

    /**
     * 获取所有电影的信息和对应的放映场次信息，根据影院编号
     * @param cinemaId
     * @return
     */
    public FilmInfoVo getFilmInfoById(int cinemaId) {
        return null;
    }

    /**
     * 根据放映场次ID获取放映信息
     * @param fieldId
     * @return
     */
    public FilmFieldVo getFilmFieldInfo(int fieldId) {
        return null;
    }

    /**
     * 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     * @param fieldId
     * @return
     */
    public FilmInfoVo getFilmInfoByFieldId(int fieldId) {
        return null;
    }






}
