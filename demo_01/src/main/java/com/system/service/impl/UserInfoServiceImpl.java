package com.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.entity.LoginUserInfo;
import com.system.entity.UserInfo;
import com.system.mapper.UserInfoMapper;
import com.system.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.system.utils.JWTTokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-02-10
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    public LoginUserInfo login(LoginUserInfo loginUserInfo) {
        // 登录
        UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>()
                .eq("loginName", loginUserInfo.getLoginName())
                .eq("loginPassword", loginUserInfo.getLoginPassword()));
        if (userInfo != null) {
            BeanUtil.copyProperties(userInfo, loginUserInfo, true);
            // 设置token
            String token = JWTTokenUtils.getToken(loginUserInfo.getUserCode().toString(), loginUserInfo.getLoginPassword());
            loginUserInfo.setToken(token);
            return loginUserInfo;
        }
        return null;
    }
}
