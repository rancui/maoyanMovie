package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class HallInfoVo implements Serializable {

    private Integer hallFieldId;
    private String hallNAme;
    private String price;
    private String seatFile;
    private String soldSeats;



}
