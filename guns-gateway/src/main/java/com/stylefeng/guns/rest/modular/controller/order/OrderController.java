package com.stylefeng.guns.rest.modular.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVo;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {

    @Reference(interfaceClass = OrderServiceAPI.class)
    private OrderServiceAPI orderServiceAPI;
    /**
     * 购票
     * @param fieldId
     * @param soldSeats
     * @param seatName
     * @return
     */
    @PostMapping("buy_tickets")
    public ServerResponse buyTickets(Integer fieldId, String soldSeats, String seatName){
        // 验证售出的票是否为真（是否为当场次的票）
        boolean isTrueSeats = orderServiceAPI.isTrueSeats(fieldId+"",soldSeats);

        // 已经销售的座位里，有没有这些座位,true未售，false已售
        boolean isNotSoldSeats = orderServiceAPI.isNotSoldSeats(fieldId+"",soldSeats);

        //是该场次的票，且座位号未售
        if(isTrueSeats && isNotSoldSeats){
            // 创建订单信息,注意获取登陆人
            String userId = CurrentUser.getCurrentUser();
            if(userId==null||userId.trim().length()==0){
                return ServerResponse.createErrorMsg("用户未登录");
            }
            OrderVo orderVo = orderServiceAPI.saveOrder(fieldId,soldSeats,seatName,Integer.parseInt(userId));
            if(orderVo==null){
                log.error("未购票成功");
                return  ServerResponse.createErrorMsg("购票业务异常");
            }
            return ServerResponse.createSuccessData(orderVo);

        }

        return ServerResponse.createErrorMsg("该场次暂无此座或该座位号已售");

    }


    @PostMapping("get_order_info")
    public ServerResponse getOrderInfo(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize){

        // 获取当前登陆人的信息
        String userId = CurrentUser.getCurrentUser();
        // 使用当前登陆人获取已经购买的订单
        if(userId!=null&& userId.trim().length()!=0){
            Page<OrderVo> orderVoPage = orderServiceAPI.getOrderByUserId(Integer.parseInt(userId));

            return ServerResponse.creatSuccessPageInfo(pageNum,(int)orderVoPage.getPages(),null,orderVoPage.getRecords());
        }

        return ServerResponse.createErrorMsg("用户未登录");


    }


}
