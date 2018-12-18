package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CinemaInfoVo implements Serializable {

     private Integer cinemaId;
     private String imgUrl;
     private String cinemaName;
     private String cinemaAddress;
     private String cinemaPhone;


}
