package com.wxy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wxy.reggie.entity.Category;
import com.wxy.reggie.mapper.CategoryMapper;

/**
 * @author wxy
 */
public interface CategoryService extends IService<Category>  {
    public void remove(Long id);
}
