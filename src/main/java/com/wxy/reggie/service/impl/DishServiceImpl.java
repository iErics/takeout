package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.entity.Dish;
import com.wxy.reggie.mapper.DishMapper;
import com.wxy.reggie.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @author wxy
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService{
}
