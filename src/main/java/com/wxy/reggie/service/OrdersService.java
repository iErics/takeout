package com.wxy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxy.reggie.entity.Orders;

/**
 * @author wxy
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders
     */
   void submit(Orders orders);

}
