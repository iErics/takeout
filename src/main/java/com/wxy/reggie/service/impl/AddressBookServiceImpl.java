package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.entity.AddressBook;
import com.wxy.reggie.mapper.AddressBookMapper;
import com.wxy.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author wxy
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
