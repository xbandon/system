package com.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.constant.ResponseConstant;
import com.system.entity.LoginUserInfo;
import com.system.entity.UserInfo;
import com.system.mapper.UserInfoMapper;
import com.system.service.UserInfoService;
import com.system.utils.JWTTokenUtils;
import com.system.wrapper.Wrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class LoginController {
    private static Logger logger = LogManager.getLogger(LoginController.class);
    @Resource
    UserInfoMapper userInfoMapper;

    @RequestMapping("/login")
    public Wrapper login(@RequestBody LoginUserInfo loginUserInfo) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String loginName = loginUserInfo.getLoginName();
            String loginPassword = loginUserInfo.getLoginPassword();

            if (StrUtil.isEmpty(loginName) || StrUtil.isEmpty(loginPassword)) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "参数错误");
            }

            UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("loginName", loginName));
            if (userInfo == null) {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "账户不存在");
            }

            UserInfo user = userInfoMapper.selectOne(new QueryWrapper<UserInfo>()
                    .eq("loginName", loginUserInfo.getLoginName())
                    .eq("loginPassword", loginUserInfo.getLoginPassword()));
            if (user != null) {
                BeanUtil.copyProperties(user, loginUserInfo, true);
                // 设置token
                String token = JWTTokenUtils.getToken(loginUserInfo.getUserCode().toString(), loginUserInfo.getLoginPassword());
                loginUserInfo.setToken(token);
                resultMap.put("data", loginUserInfo);
            } else {
                return Wrapper.info(ResponseConstant.ERROR_CODE, "账户名与密码不匹配");
            }

        } catch (Exception e) {
            return Wrapper.error();
        }
        return Wrapper.success(resultMap);
    }

}
