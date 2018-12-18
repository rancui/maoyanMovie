package com.stylefeng.guns.rest.modular.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.Area;
import com.stylefeng.guns.rest.common.persistence.model.Brand;
import com.stylefeng.guns.rest.common.persistence.model.Cinema;
import com.stylefeng.guns.rest.common.persistence.model.Hall;
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
        boolean flag = false;
        Brand brand = brandMapper.selectById(brandId);

        //判断brandId是否等于99（全部）
        if(brandId==99||brand==null||brand.getUuid()==null){
            flag = true;
        }

        List<Brand> brandList = brandMapper.selectList(null);
        List<BrandVo> brandVoList = Lists.newArrayList();
        for(Brand brandItem:brandList){
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandName(brandItem.getShowName());
            brandVo.setBrandId(brandItem.getUuid());
            //如果flag为true，则需要99，如果为false，则匹配上的内容为active
            if(flag){
                if(brandItem.getUuid()==99){
                    brandVo.setActive(true);
                }
            }else {
                if(brandItem.getUuid()==brandId){
                    brandVo.setActive(true);
                }
            }
            brandVoList.add(brandVo);
        }

        return brandVoList;
    }

    /**
     * 获取地区列表
     * @param areaId
     * @return
     */
    public List<AreaVo> getAreas(int areaId){
        boolean flag = false;
        Area area = areaMapper.selectById(areaId);
        //判断areaId是否等于99（全部）
        if(areaId==99||area==null||area.getUuid()==null){
            flag = true;
        }

        List<Area> areaList = areaMapper.selectList(null);
        List<AreaVo> areaVoList = Lists.newArrayList();
        for(Area areaItem:areaList){
            AreaVo areaVo = new AreaVo();
            areaVo.setAreaName(areaItem.getShowName());
            areaVo.setAreaId(areaItem.getUuid());
            //如果flag为true，则需要99，如果为false，则匹配上的内容为active
            if(flag){
                if(areaItem.getUuid()==99){
                    areaVo.setActive(true);
                }
            }else {
                if(areaItem.getUuid()==areaId){
                    areaVo.setActive(true);
                }
            }
            areaVoList.add(areaVo);
        }
        return areaVoList;
    }


    /**
     * 获取影厅类型列表
     * @param hallType
     * @return
     */
    public List<HallTypeVo> getHallType(int hallType) {
        boolean flag = false;
        Hall hall = hallMapper.selectById(hallType);
        //判断hallType是否等于99（全部）
        if(hallType==99||hall==null||hall.getUuid()==null){
            flag = true;
        }

        List<Hall> hallList = hallMapper.selectList(null);
        List<HallTypeVo> hallTypeVoList = Lists.newArrayList();
        for(Hall hallItem:hallList){
            HallTypeVo hallTypeVo = new HallTypeVo();
            hallTypeVo.setHallTypeName(hallItem.getShowName());
            hallTypeVo.setHallTypeId(hallItem.getUuid());
            //如果flag为true，则需要99，如果为false，则匹配上的内容为active
            if(flag){
                if(hallItem.getUuid()==99){
                    hallTypeVo.setActive(true);
                }
            }else {
                if(hallItem.getUuid()==hallType){
                    hallTypeVo.setActive(true);
                }
            }
            hallTypeVoList.add(hallTypeVo);
        }
        return hallTypeVoList;
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
