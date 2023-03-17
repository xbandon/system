package com.system.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.entity.UserInfo;
import com.system.mapper.UserInfoMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
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
    UserInfoMapper userInfoMapper;

    @Transactional
    @RequestMapping("/login")
    public Map<String, Object> login(@RequestBody String json) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            JSONObject object = JSON.parseObject(json);
            String loginName = object.getString("loginName");
            String loginPassword = object.getString("loginPassword");

            if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(loginPassword)) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "参数错误！");
            }

            // 账户名存在检查
            Long isExist = userInfoMapper.selectCount(new QueryWrapper<UserInfo>()
                    .eq("loginName", loginName));
            if (isExist == 0) {
                resultMap.put("error", false);
                resultMap.put("errMsg", "账户名不存在！");
            } else {
                // 登录
                Long flg = userInfoMapper.selectCount(new QueryWrapper<UserInfo>()
                        .eq("loginName", loginName)
                        .eq("loginPassword", DigestUtils.md5DigestAsHex(loginPassword.getBytes())));
                if (flg > 0) {
                    resultMap.put("success", true);
                } else {
                    resultMap.put("error", false);
                    resultMap.put("errMsg", "密码错误！");
                }
            }

        } catch (Exception e) {
            resultMap.put("error", false);
            resultMap.put("errMsg", "系统异常！");
        }
        return resultMap;
    }

}
