package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.entity.User;
import com.wxy.reggie.mapper.UserMapper;
import com.wxy.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author wxy
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
}
