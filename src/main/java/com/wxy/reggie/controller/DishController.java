package com.wxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxy.reggie.common.R;
import com.wxy.reggie.dto.DishDto;
import com.wxy.reggie.entity.Category;
import com.wxy.reggie.entity.Dish;
import com.wxy.reggie.entity.DishFlavor;
import com.wxy.reggie.service.CategoryService;
import com.wxy.reggie.service.DishFlavorService;
import com.wxy.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wxy
 */

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page> getDish(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page(page, pageSize);
        Page<DishDto> dtoPage = new Page();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            if (categoryId != null) {
                Category category = categoryService.getById(categoryId);
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    /*@GetMapping ("/list")
    public R<List<Dish>> getDish(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 根据 CategoryId 查询
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        // 根据 status 查询状态为1 (起售状态) 的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        // 排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        // 查询
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }*/

    @GetMapping ("/list")
    public R<List<DishDto>> getDish(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 根据 CategoryId 查询
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        // 根据 status 查询状态为1 (起售状态) 的菜品
        queryWrapper.eq(Dish::getStatus, 1);
        // 排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        // 查询
        List<Dish> dishList = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = dishList.stream().map((dishItem) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dishItem, dishDto);

            Long categoryId = dishItem.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (categoryId != null) {
                String categoryName= category.getName();
                dishDto.setCategoryName(categoryName);
            }
            
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishItem.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());


        return R.success(dishDtoList);
    }

    @PostMapping("/status/{status}")
    public R<String> updateDish(@PathVariable Long status, Long[] ids) {
        List<Dish> dishList = new ArrayList<>();
        for (Long id:ids
             ) {
            Dish dish = new Dish();
            dish.setStatus(status);
            dish.setId(id);
            dishList.add(dish);
        }
        boolean b = dishService.updateBatchById(dishList);
        if (b){
            return R.success("更改成功");
        }
        return R.success("更改失败");
    }

    @PostMapping
    public R<String> add(@RequestBody Dish dish) {
        return null;
    }

}
