package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.order.vo.OrderVo;
import com.stylefeng.guns.rest.common.persistence.model.Order2017;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author rancui
 * @since 2018-12-26
 */
public interface Order2017Mapper extends BaseMapper<Order2017> {
    String getSeatPathByFieldId(String fieldId);
    OrderVo getOrderVOByUuid(String orderId);
    List<OrderVo> getOrdersByUserId(Integer userId);
    String getSoldSeatsByFieldId(Integer fieldId);

}
