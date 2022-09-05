package com.wxy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wxy.reggie.common.R;
import com.wxy.reggie.entity.User;
import com.wxy.reggie.service.UserService;
import com.wxy.reggie.utils.SMSUtils;
import com.wxy.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author wxy
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @Value(value = "${aliyun.dysms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.dysms.accessKeySecret}")
    private String accessKeySecret;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) throws Exception {

        System.out.println(accessKeyId);
        String phone = user.getPhone();
        if (phone != null){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
            SMSUtils.sendMsg(phone, code, accessKeyId, accessKeySecret);
            session.setAttribute(phone, code);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @RequestMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = map.get("phone").toString();

        String code = map.get("code").toString();

        String codeInSession = session.getAttribute(phone).toString();

        if (codeInSession != null && code.equals(codeInSession)) {
            final LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                User newUser = new User();
                newUser.setPhone(phone);
                newUser.setStatus(1);
                userService.save(newUser);
                session.setAttribute("user", newUser.getId());
                return R.success(newUser);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }
}
