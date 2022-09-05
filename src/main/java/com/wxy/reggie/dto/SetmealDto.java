package com.wxy.reggie.dto;

import com.wxy.reggie.entity.Setmeal;
import com.wxy.reggie.entity.SetmealDish;
import com.wxy.reggie.entity.Setmeal;
import com.wxy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
