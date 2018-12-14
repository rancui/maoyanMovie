package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryVo implements Serializable {

    private Integer categoryId;
    private String categoryName;
    private boolean isActive;

}
