package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.entity.Employee;
import com.wxy.reggie.mapper.EmployeeMapper;
import com.wxy.reggie.service.EmployService;
import org.springframework.stereotype.Service;

/**
 * @author wxy
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployService {


}
