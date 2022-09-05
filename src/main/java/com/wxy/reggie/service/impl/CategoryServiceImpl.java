package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.common.CustomException;
import com.wxy.reggie.entity.Category;
import com.wxy.reggie.entity.Dish;
import com.wxy.reggie.entity.Setmeal;
import com.wxy.reggie.mapper.CategoryMapper;
import com.wxy.reggie.service.CategoryService;
import com.wxy.reggie.service.DishService;
import com.wxy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wxy
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件， 根据分类id进行查询
        dishQueryWrapper.eq(Dish::getCategoryId, id);

        long dishCount = dishService.count(dishQueryWrapper);

        if (dishCount > 0) {
            throw new CustomException("当前分类已关联菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件， 根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);

        long setmealCount = setmealService.count(setmealLambdaQueryWrapper);

        if (setmealCount > 0) {
            throw new  CustomException("当前分类已关联套餐，不能删除");
        }

        super.removeById(id);

    }
}
