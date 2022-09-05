package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.entity.DishFlavor;
import com.wxy.reggie.mapper.DishFlavorMapper;
import com.wxy.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @author wxy
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
