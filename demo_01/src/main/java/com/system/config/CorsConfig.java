package com.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // 当前跨域请求最大有效时长
    private static final long MAX_AGE = 24 * 60 * 60; // 默认一天

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 设置访问源地址
        configuration.addAllowedOrigin("*");
        // 设置访问源请求头
        configuration.addAllowedHeader("*");
        // 设置访问源请求方法
        configuration.addAllowedMethod("*");
        configuration.setMaxAge(MAX_AGE);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

}
