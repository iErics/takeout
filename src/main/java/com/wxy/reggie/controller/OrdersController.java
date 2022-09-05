package com.wxy.reggie.controller;

import com.wxy.reggie.common.BaseContext;
import com.wxy.reggie.common.R;
import com.wxy.reggie.entity.Orders;
import com.wxy.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author wxy
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @RequestMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("订单添加成功");
    }

}
