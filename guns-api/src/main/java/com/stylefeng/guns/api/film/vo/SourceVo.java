package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SourceVo implements Serializable {
    private Integer sourceId;
    private String sourceName;
    private boolean isActive;
}
