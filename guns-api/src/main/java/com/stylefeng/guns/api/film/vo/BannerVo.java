package com.stylefeng.guns.api.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BannerVo implements Serializable {
    private Integer bannerId;
    private String bannerAddress;
    private String bannerUrl;
}
