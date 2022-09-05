package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.entity.ShoppingCart;
import com.wxy.reggie.mapper.ShoppingCartMapper;
import com.wxy.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author wxy
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
