package com.stylefeng.guns.api.film.vo;

import lombok.Data;

/**
 * 影片列表页
 */

@Data
public class FilmListVo {
    private Integer showType = 1;
    private Integer sortId = 1;
    private Integer categoryId = 99;
    private Integer sourceId = 99;
    private Integer yearId = 99;
    private Integer pageNum = 1;
    private  Integer pageSize = 18;

}
