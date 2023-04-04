package com.system.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.system.config.exception.ServiceException;
import com.system.constant.ResponseConstant;
import com.system.entity.UserInfo;
import com.system.mapper.UserInfoMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xrx
 * @since 2023/4/3
 * description TODO
 */
@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");
        // 若没有映射到方法则通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // Token非空检查
        if (StrUtil.isEmpty(token)) {
            throw new ServiceException("无Token");
        }

        // 获取Token中的userCode
        String userCode = null;
        try {
            userCode = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new ServiceException("获取Token失败");
        }

        UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("userCode", userCode));
        if (userInfo == null) {
            throw new ServiceException("用户不存在");
        }

        // 验证Token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(userInfo.getLoginPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException("Token验证失败");
        }

        return true;
    }
}