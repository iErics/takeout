package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.common.CustomException;
import com.wxy.reggie.dto.SetmealDto;
import com.wxy.reggie.entity.Setmeal;
import com.wxy.reggie.entity.SetmealDish;
import com.wxy.reggie.mapper.SetmealMapper;
import com.wxy.reggie.service.SetmealDishService;
import com.wxy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wxy
 */

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息,操作setmeal, 执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        return setmealDishService.saveBatch(setmealDishes);
    }


    /**
     * 删除套餐, 同时删除套餐和菜品的关联数据
     * @param ids
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeWithDish(List<Long> ids) {
        //判断是否在售,在售则不可删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        long count = this.count(queryWrapper);
        if (count > 0) {
            // 如果不可删除,则抛出一个业务异常
            throw new CustomException("当前套餐在售,不可删除");
        }

        // 删除套餐
        this.removeByIds(ids);

        //删除关联菜品
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);
    }

}
