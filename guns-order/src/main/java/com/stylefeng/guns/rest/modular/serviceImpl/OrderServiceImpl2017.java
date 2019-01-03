package com.stylefeng.guns.rest.modular.serviceImpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVo;
import com.stylefeng.guns.api.cinema.vo.OrderNeedVo;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVo;
import com.stylefeng.guns.core.util.BigDecimalUtil;
import com.stylefeng.guns.rest.common.Const;
import com.stylefeng.guns.rest.common.persistence.dao.Order2017Mapper;
import com.stylefeng.guns.rest.common.persistence.model.Order2017;
import com.stylefeng.guns.rest.common.persistence.model.OrderDefault;
import com.stylefeng.guns.rest.common.util.FTPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Service(interfaceClass = OrderServiceAPI.class,group = "order2017")
public class OrderServiceImpl2017 implements OrderServiceAPI {
    @Autowired
    private Order2017Mapper order2017Mapper;
    @Autowired
    private FTPUtil ftpUtil;
    @Reference(interfaceClass = CinemaServiceAPI.class)
    private CinemaServiceAPI cinemaServiceAPI;

    /**
     * 验证该座位号是否真实存在
     * @param fieldId
     * @param seats
     * @return
     */
    @Override
    public boolean isTrueSeats(String fieldId, String seats) {

        // 根据FieldId找到对应的座位位置图
        String seatPath = order2017Mapper.getSeatPathByFieldId(fieldId);
        // 读取位置图，判断seats是否为真
        String fileStrByAddress  = ftpUtil.getFileStrByAddress(seatPath);
        // 将fileStrByAddress转换为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(fileStrByAddress);
        String[] idsArr = jsonObject.get("ids").toString().split(",");
        String[] seatsArr = seats.split(",");
        int count = 0;
        for (String id:idsArr){
            for (String seat:seatsArr){
                if(seat.equalsIgnoreCase(id)){
                    count++;
                }
            }
        }
        // 如果匹配上的数量与已售座位数一致，则表示全都匹配上了
        if(seatsArr.length==count){
            return true;
        }else {
            return false;
        }
    }

    /**
     *  判断是否为已售座位，true未售，false已售
     * @param fieldId 放映场次
     * @param seats 用户选择的座位号
     * @return
     */
    @Override
    public boolean isNotSoldSeats(String fieldId, String seats) {

        EntityWrapper entityWrapper = new EntityWrapper();
        entityWrapper.eq("field_id",fieldId);
        List<Order2017> orderList = order2017Mapper.selectList(entityWrapper);
        String[] seatArr = seats.split(",");
        for(Order2017 order:orderList){
            String[] idArr= order.getSeatsIds().split(",");
            for(String id:idArr){//遍历已售订单中的座位号
              for(String seat:seatArr){//遍历用户下单的座位号
                  if(id.equalsIgnoreCase(seat)){ //如果所有订单中有任意一个与用户所选座位号相同，则说明该座位已售，返回false.
                      return false;
                  }
              }
            }
        }

        return true;
    }

    /**
     *  保存订单
     * @param fieldId
     * @param soldSeats
     * @param seatsName
     * @param userId
     * @return
     */
    @Override
    public OrderVo saveOrder(Integer fieldId, String soldSeats, String seatsName, Integer userId) {
        //订单编号
        String uuid = UUID.randomUUID().toString().replace("-","");
        //影片信息
        FilmInfoVo filmInfoVo = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);
        Integer filmId = filmInfoVo.getFilmId();

        //影院信息
        OrderNeedVo orderNeedVo = cinemaServiceAPI.getOrderNeed(fieldId);
        Integer cinemaId = orderNeedVo.getCinemaId();
        double  filmPrice = Double.parseDouble(orderNeedVo.getFilmPrice());

        //求订单总额
        int orderCount = soldSeats.split(",").length;
        double orderTotalPrice = BigDecimalUtil.multiply(orderCount,filmPrice).doubleValue();

        Order2017 order2017 = new Order2017();
        order2017.setUuid(uuid);
        order2017.setCinemaId(cinemaId);
        order2017.setFieldId(fieldId);
        order2017.setFilmId(filmId);
        order2017.setOrderPrice(orderTotalPrice);
        order2017.setFilmPrice(filmPrice);
        order2017.setOrderUser(userId);
        order2017.setSeatsIds(soldSeats);
        order2017.setSeatsName(seatsName);

        Integer count = order2017Mapper.insert(order2017);
        if(count>0){
           OrderVo orderVo = order2017Mapper.getOrderVOByUuid(uuid);
            if(orderVo==null||orderVo.getOrderId()==null){
                return null;
            }

            return orderVo;
        }

        log.error("订单插入失败");
        return null;
    }

    /**
     * 使用当前登陆人获取已经购买的订单
     * @param userId
     * @return
     */
    @Override
    public Page<OrderVo> getOrderByUserId(Integer userId) {

        Page<OrderVo>  orderVoPage = new Page<>();
        if(userId==null){
            log.error("用户编号未传入,订单查询业务失败");
            return null;
        }

        List<OrderVo> orderVoList = order2017Mapper.getOrdersByUserId(userId);
        if(orderVoList==null||orderVoList.size()==0){
            orderVoPage.setTotal(0);
            orderVoPage.setRecords(new ArrayList<>());
            return orderVoPage;
        }else {
            // 获取订单总数
            EntityWrapper<Order2017> entityWrapper = new EntityWrapper<>();
            entityWrapper.eq("order_user",userId);
            Integer count = order2017Mapper.selectCount(entityWrapper);
            // 将结果放入Page
            orderVoPage.setTotal(count);
            orderVoPage.setRecords(orderVoList);
            return orderVoPage;
        }
    }

    /**
     * 根据FieldId 获取所有已经销售的座位编号
     * @param fieldId 放映场次
     * @return
     */
    @Override
    public String getSoldSeatsByFieldId(Integer fieldId) {
        if(fieldId==null){
            log.error("查询已售座位错误，未传入任何放映场次编号");
            return "";
        }

        String SoldSeats = order2017Mapper.getSoldSeatsByFieldId(fieldId);
        return SoldSeats;
    }

    /**
     * 获取订单信息
     * @param orderId
     * @return
     */

    @Override
    public OrderVo getOrderInfoByOrderId(String orderId) {

        OrderVo orderVo = order2017Mapper.getOrderVOByUuid(orderId);
        return orderVo;
    }


    /**
     * 支付成功
     * @param orderId
     * @return
     */
    @Override
    public boolean paySuccess(String orderId) {

        Order2017 order2017 = new Order2017();
        order2017.setUuid(orderId);
        order2017.setOrderStatus(Const.OrderStatusEnum.PAY_SUCCESS.getCode());

        int count = order2017Mapper.updateById(order2017);
        if(count>0){
            return true;
        }
        return false;
    }

    /**
     * 支付失败
     * @param orderId
     * @return
     */
    @Override
    public boolean payFail(String orderId) {

        Order2017 order2017 = new Order2017();
        order2017.setUuid(orderId);
        order2017.setOrderStatus(Const.OrderStatusEnum.PAY_Fail.getCode());

        int count = order2017Mapper.updateById(order2017);
        if(count>0){
            return true;
        }
        return false;

    }










}
