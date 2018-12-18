package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InfoRequestVo implements Serializable {
    private String biography;
    private ActorRequestVo actors;
    private ImgVo imgVO;
    private String filmId;
}
