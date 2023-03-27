package com.system.controller;

import cn.hutool.core.util.StrUtil;
import com.system.entity.LoginUserInfo;
import com.system.service.UserInfoService;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @RequestMapping("/login")
    public Map<String, Object> login(@RequestBody LoginUserInfo loginUserInfo) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String loginName = loginUserInfo.getLoginName();
            String loginPassword = loginUserInfo.getLoginPassword();

            if (StrUtil.isEmpty(loginName) || StrUtil.isEmpty(loginPassword)) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "参数错误！");
            }

            LoginUserInfo userInfo = userInfoService.login(loginUserInfo);
            if (userInfo != null) {
                resultMap.put("user", userInfo);
                resultMap.put("success", true);
            } else {
                resultMap.put("error", false);
                resultMap.put("errMsg", "账户名与密码不匹配！");
            }

        } catch (Exception e) {
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统异常！");
        }
        return resultMap;
    }

}
