package com.wxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wxy.reggie.common.R;
import com.wxy.reggie.entity.Employee;
import com.wxy.reggie.service.EmployService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author wxy
 */

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployService employService;

    /**
     * 员工登陆
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        // 将用户提交的密码进行md5加密
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());

        //根据用户提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employService.getOne(queryWrapper);

        // 如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("用户不存在");
        }

        //密码比对，如果不一致则返回密码错误提示
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        //验证用户是否被禁用，如禁用则返回用户已禁用
        if (emp.getStatus() != 1) {
            return R.error("用户已禁用");
        }

        //如果验证都通过则返回用户信息
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    /**
     * 用户退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 如何新增员工
     * 数据如何校验，校验失败如何处理
     * @param employee
     * @return
     */
    @PostMapping()
    public R<String> add(HttpServletRequest request,@RequestBody Employee employee) {

        String password = DigestUtils.md5DigestAsHex(employee.getIdNumber().substring(12).getBytes());
        employee.setPassword(password);
        //  employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        //Long empId = (Long) request.getSession().getAttribute("employee");

        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);

        employService.save(employee);
        log.info("新增员工{}", employee.toString());
        return R.success("新增员工成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={}, pageSize={}, name={}", page, pageSize, name);
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        // Long empId = (Long) request.getSession().getAttribute("employee");
        // employee.setUpdateUser(empId);
        // employee.setUpdateTime(LocalDateTime.now());
        boolean update = employService.updateById(employee);
        return R.success("账号状态更改成功");
    }

    @GetMapping ("/{id}")
    public R<Employee> edit(@PathVariable Long id) {
        Employee employee = employService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询此员工信息");
    }
}
