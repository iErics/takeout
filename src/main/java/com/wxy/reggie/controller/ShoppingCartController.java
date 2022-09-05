package com.wxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxy.reggie.common.R;
import com.wxy.reggie.entity.ShoppingCart;
import com.wxy.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wxy
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, ShoppingCart::getUserId, userId);

        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }
        if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }

        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if (one != null) {
            one.setNumber(one.getNumber() + 1);
            shoppingCartService.updateById(one);
            return R.success(one);
        }
        shoppingCart.setUserId(userId);
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartService.save(shoppingCart);
        return R.success(shoppingCart);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, ShoppingCart::getUserId, userId);

        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }
        if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }

        ShoppingCart shoppingCartOne = shoppingCartService.getOne(queryWrapper);

        if (shoppingCartOne != null) {
            shoppingCartOne.setNumber(shoppingCartOne.getNumber() - 1);
            shoppingCartService.updateById(shoppingCartOne);
            return R.success(shoppingCart);
        }
        return R.error("未查询到记录");
    }

    @RequestMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        // 查出 number 为0的记录并删除
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getNumber, 0);
        shoppingCartService.remove(lambdaQueryWrapper);

        // 查询用户的购物车列表
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, ShoppingCart::getUserId, userId);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(HttpSession session) {
        Long userid = (Long) session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userid != null, ShoppingCart::getUserId, userid);
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

}
