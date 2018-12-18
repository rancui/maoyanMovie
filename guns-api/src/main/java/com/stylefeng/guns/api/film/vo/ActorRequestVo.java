package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ActorRequestVo implements Serializable {
    private ActorVo director;
    private List<ActorVo> actors;
}
