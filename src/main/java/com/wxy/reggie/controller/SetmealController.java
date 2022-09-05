package com.wxy.reggie.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxy.reggie.common.R;
import com.wxy.reggie.dto.SetmealDto;
import com.wxy.reggie.entity.Category;
import com.wxy.reggie.entity.Dish;
import com.wxy.reggie.entity.Setmeal;
import com.wxy.reggie.service.CategoryService;
import com.wxy.reggie.service.DishService;
import com.wxy.reggie.service.SetmealDishService;
import com.wxy.reggie.service.SetmealService;
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
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, page, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            if (categoryId != null) {
                Category category = categoryService.getById(categoryId);
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    @PostMapping
    public R<String> addSetmeal(@RequestBody SetmealDto setmealDto) {

        boolean save = setmealService.saveWithDish(setmealDto);
        if (save) {
            return R.success("新增套餐成功");
        }
        return R.success("新增套餐失败");
    }

    @PostMapping("/status/{status}")
    public R<String> updateSetmeal(@PathVariable Long status, Long[] ids) {
        List<Setmeal> list = new ArrayList<>();
        for (Long id:ids
             ) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setId(id);
            list.add(setmeal);
        }
        boolean b = setmealService.updateBatchById(list);
        if (b) {
            return R.success("更新成功");
        }
        return R.success("更新失败");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        Long categoryId = setmeal.getCategoryId();
        Long status = setmeal.getStatus();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Setmeal::getCategoryId, categoryId);
        queryWrapper.eq(status != null, Setmeal::getStatus, 1);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        final List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
