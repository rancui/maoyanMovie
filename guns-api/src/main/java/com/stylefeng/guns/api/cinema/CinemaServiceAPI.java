package com.stylefeng.guns.api.cinema;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.vo.*;

import java.util.List;

public interface CinemaServiceAPI {

    /**
     * 根据入参查询影院列表
     * @param cinemaQueryVo
     * @return
     */
     Page<CinemaVo> getCinemas(CinemaQueryVo cinemaQueryVo);
    /**
     * 根据条件获取品牌列表【除了99（全部）外，其他的数字为isActive】
     * @param brandId
     * @return
     */
     List<BrandVo> getBrands(int brandId);

    /**
     * 获取地区列表
     * @param areaId
     * @return
     */
     List<AreaVo> getAreas(int areaId);


    /**
     * 获取影厅类型列表
     * @param hallType
     * @return
     */
     List<HallTypeVo> getHallType(int hallType);


    /**
     * 根据影院编号，获取影院信息
     * @param cinemaId
     * @return
     */
     CinemaInfoVo getCinemaInfoById(int cinemaId);

    /**
     * 获取所有电影的信息和对应的放映场次信息，根据影院编号
     * @param cinemaId
     * @return
     */
    List<FilmInfoVo> getFilmInfoById(int cinemaId);
    /**
     * 根据放映场次ID获取放映信息
     * @param fieldId
     * @return
     */
     HallInfoVo getFilmFieldInfo(int fieldId);

    /**
     * 根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
     * @param fieldId
     * @return
     */
     FilmInfoVo getFilmInfoByFieldId(int fieldId);

}
