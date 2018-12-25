package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class OrderNeedVo implements Serializable {
    private Integer cinemaId;
    private String filmPrice;
}
