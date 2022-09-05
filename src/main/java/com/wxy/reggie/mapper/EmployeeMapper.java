package com.wxy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wxy.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wxy
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
