package com.system.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JWTTokenUtils {
    private JWTTokenUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 生成token
     *
     * @param userId
     * @param sign
     * @return
     */
    public static String getToken(String userId, String sign) {
        return JWT.create()
                //签收者
                .withAudience(userId)
                //主题
                .withSubject("token")
                //2小时后token过期
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                //以password作为token的密钥
                .sign(Algorithm.HMAC256(sign));
    }
}