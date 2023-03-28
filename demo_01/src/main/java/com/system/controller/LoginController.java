package com.system.controller;

import cn.hutool.core.util.StrUtil;
import com.system.constant.ResponseConstant;
import com.system.entity.LoginUserInfo;
import com.system.service.UserInfoService;
import com.system.wrapper.Wrapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {
    @Resource
    UserInfoService userInfoService;

    @RequestMapping("/login")
    public Wrapper login(@RequestBody LoginUserInfo loginUserInfo) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String loginName = loginUserInfo.getLoginName();
            String loginPassword = loginUserInfo.getLoginPassword();

            if (StrUtil.isEmpty(loginName) || StrUtil.isEmpty(loginPassword)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            LoginUserInfo userInfo = userInfoService.login(loginUserInfo);
            if (userInfo != null) {
                resultMap.put("data", userInfo);
            } else {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "账户名与密码不匹配");
            }

        } catch (Exception e) {
            return Wrapper.error();
        }
        return Wrapper.success(resultMap);
    }

}
