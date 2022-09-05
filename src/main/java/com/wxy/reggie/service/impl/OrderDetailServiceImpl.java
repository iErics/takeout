package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.entity.OrderDetail;
import com.wxy.reggie.mapper.OrderDetailMapper;
import com.wxy.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author wxy
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
