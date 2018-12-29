package com.stylefeng.guns.rest.modular.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.stylefeng.guns.api.alipay.AlipayServiceAPI;
import com.stylefeng.guns.api.alipay.vo.AlipayInfoVo;
import com.stylefeng.guns.api.alipay.vo.AlipayResultVo;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import com.stylefeng.guns.api.order.vo.OrderVo;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {

    public static final  String IMG_PRE = "http://www.rc.com";


    @Reference(interfaceClass = OrderServiceAPI.class,check = false,group ="order2018" )
    private OrderServiceAPI orderServiceAPI;

    @Reference(interfaceClass = OrderServiceAPI.class,check = false,group ="order2017" )
    private OrderServiceAPI orderServiceAPI2017;

    @Reference(interfaceClass = AlipayServiceAPI.class,check = false)
    private AlipayServiceAPI alipayServiceAPI;



    //此处做Hystrix的fallbackMethod降级服务
    public ServerResponse error(Integer fieldId, String soldSeats, String seatName){
        return ServerResponse.createErrorMsg("抱歉，下单的人太多了，请稍后重试");
    }

    /**
     * 购票
     * @param fieldId
     * @param soldSeats
     * @param seatName
     * @return
     */
    /* 在一个分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，
       如何能够保证在一个依赖出问题的情况下，不会导致整体服务失败，这个就是Hystrix需要做的事情。
       Hystrix提供了熔断、隔离、Fallback、cache、监控等功能，能够在一个、或多个依赖同时出现问题时保证系统依然可用。
       Hystrix除了熔断外，还有以下保护机制，用以保护线程的安全性：
        信号量隔离
        线程池隔离
        线程切换
     */
    @HystrixCommand(fallbackMethod = "error", commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"),
                    @HystrixProperty(name = "maxQueueSize", value = "10"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "8"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1500")
            })
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

    /**
     * 获取用户订单信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping("get_order_info")
    public ServerResponse getOrderInfo(@RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize){

        // 获取当前登陆人的信息
        String userId = CurrentUser.getCurrentUser();
        // 使用当前登陆人获取已经购买的订单
        if(userId!=null&& userId.trim().length()!=0){
            Page<OrderVo> orderVoPage2017 = orderServiceAPI2017.getOrderByUserId(Integer.parseInt(userId));
            Page<OrderVo> orderVoPage2018 = orderServiceAPI.getOrderByUserId(Integer.parseInt(userId));

            //合并总页数
            int totalPages = (int)(orderVoPage2017.getPages()+orderVoPage2018.getPages());
            //合并订单列表
            List<OrderVo> orderVoList = Lists.newArrayList();
            orderVoList.addAll(orderVoPage2017.getRecords());
            orderVoList.addAll(orderVoPage2018.getRecords());

            return ServerResponse.creatSuccessPageInfo(pageNum,totalPages,null,orderVoList);
        }

        return ServerResponse.createErrorMsg("用户未登录");

    }

    /**
     * 获取支付二维码
     * @param orderId
     * @return
     */
    @PostMapping("get_pay_info")
    public ServerResponse getPayInfo(@RequestParam("orderId") String orderId){
        String userId = CurrentUser.getCurrentUser();
        if(userId==null||userId.trim().length()==0){
            return ServerResponse.createErrorMsg("抱歉，用户未登录");
        }
        //订单二维码
        AlipayInfoVo alipayInfoVo = alipayServiceAPI.getQRCode(orderId);
        return ServerResponse.createSuccessImgPreData(IMG_PRE,null,alipayInfoVo);
    }



//
//    public ServerResponse tryNumsMore(@RequestParam("orderId") String orderId,@RequestParam("tryNums")Integer tryNums){
//
//            return ServerResponse.createErrorMsg("订单支付失败，请稍后重试");
//
//    }


    /**
     * 获取支付结果
     * @param orderId 订单编号
     * @param tryNums 重试次数，默认是1,超过4次则超时
     * @return
     */
//    @HystrixCommand(fallbackMethod = "tryNumsMore", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"),
//            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
//            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")},
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "1"),
//                    @HystrixProperty(name = "maxQueueSize", value = "10"),
//                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"),
//                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "8"),
//                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
//                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1500")
//            })
    @PostMapping("get_pay_result")
    public ServerResponse getPayResult(@RequestParam("orderId") String orderId,@RequestParam("tryNums")Integer tryNums){

        String userId = CurrentUser.getCurrentUser();
        if(userId==null||userId.trim().length()==0){
            return ServerResponse.createErrorMsg("抱歉，用户未登录");
        }

        if(tryNums>=4){
            return ServerResponse.createErrorMsg("订单支付失败，请稍后重试");
        }

        AlipayResultVo alipayResultVo = alipayServiceAPI.getOrderStatus(orderId);

        return ServerResponse.createSuccessData(alipayResultVo);

    }









}
