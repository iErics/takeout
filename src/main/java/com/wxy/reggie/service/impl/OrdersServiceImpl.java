package com.wxy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wxy.reggie.common.BaseContext;
import com.wxy.reggie.common.CustomException;
import com.wxy.reggie.entity.*;
import com.wxy.reggie.mapper.OrdersMapper;
import com.wxy.reggie.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author wxy
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    private ShoppingCartService shoppingCartService;
    private AddressBookService addressBookService;
    private OrderDetailService orderDetailService;
    private UserService userService;

    public OrdersServiceImpl(ShoppingCartService shoppingCartService, AddressBookService addressBookService, OrderDetailService orderDetailService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.addressBookService = addressBookService;
        this.orderDetailService = orderDetailService;
        this.userService = userService;
    }




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(Orders orders) {
        //获取用户ID
        Long userId = BaseContext.getCurrentId();


        // 查询购物车信息
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空, 不能下单");
        }

        User user = userService.getById(userId);

        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        // 设置订单数据
        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setPhone(user.getPhone());
        orders.setUserName(user.getName());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getDistrictName())
                        + (addressBook.getCityName() == null? "" : addressBook.getCityName())
                        + (addressBook.getDistrictName() == null? "" : addressBook.getDistrictName())
                        + (addressBook.getDetail() == null? "" : addressBook.getDetail()));

        // 保存订单数据
        this.save(orders);

        // 保存订单详情
        orderDetailService.saveBatch(orderDetails);

        // 清空购物车
        shoppingCartService.removeById(userId);
    }
}
