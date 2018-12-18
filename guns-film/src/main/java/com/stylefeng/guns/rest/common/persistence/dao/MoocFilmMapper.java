package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.FilmDetailVo;
import com.stylefeng.guns.rest.common.persistence.model.MoocFilm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author rancui
 * @since 2018-12-13
 */
public interface MoocFilmMapper extends BaseMapper<MoocFilm> {

    FilmDetailVo getFilmDetailByName(@Param("filmName") String filmName);
    FilmDetailVo getFilmDetailByUUId(@Param("uuid") String uuid);

}
