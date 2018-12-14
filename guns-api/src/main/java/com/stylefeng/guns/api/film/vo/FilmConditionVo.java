package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.util.List;

/**
 * 按条件查询影片
 */
@Data
public class FilmConditionVo {
    private List<CategoryVo> categoryVoList;
    private List<SourceVo> sourceVoList;
    private List<YearVo> yearVoList;
}
