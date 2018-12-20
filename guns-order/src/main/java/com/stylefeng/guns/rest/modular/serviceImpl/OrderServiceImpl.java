package com.stylefeng.guns.rest.modular.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.api.order.OrderServiceAPI;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = OrderServiceAPI.class)
public class OrderServiceImpl implements OrderServiceAPI {





}
