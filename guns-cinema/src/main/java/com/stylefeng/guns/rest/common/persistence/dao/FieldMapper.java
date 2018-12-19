package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.cinema.vo.FilmInfoVo;
import com.stylefeng.guns.api.cinema.vo.HallInfoVo;
import com.stylefeng.guns.rest.common.persistence.model.Field;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 放映场次表 Mapper 接口
 * </p>
 *
 * @author rancui
 * @since 2018-12-18
 */
public interface FieldMapper extends BaseMapper<Field> {

    List<FilmInfoVo> getFilmInfos(@Param("cinemaId") int cinemaId);
    HallInfoVo getHallInfo(int fieldId);
    FilmInfoVo getFilmInfoByFieldId(int fieldId);


}
