package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActorVo implements Serializable {
    private String imgAddress;
    private String directorName;
    private String roleName;
}
