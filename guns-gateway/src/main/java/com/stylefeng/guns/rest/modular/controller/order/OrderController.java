package com.stylefeng.guns.rest.modular.controller.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/")
public class OrderController {

    @Reference(interfaceClass = OrderServiceAPI.class)
    private OrderServiceAPI orderServiceAPI;


}
