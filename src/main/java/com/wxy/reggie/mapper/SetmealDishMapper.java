package com.wxy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxy.reggie.dto.SetmealDto;
import com.wxy.reggie.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
}
