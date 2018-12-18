package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.ActorVo;
import com.stylefeng.guns.rest.common.persistence.model.MoocActor;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author rancui
 * @since 2018-12-13
 */
public interface MoocActorMapper extends BaseMapper<MoocActor> {

    List<ActorVo> getActors(String filmId);

}
