package com.smlj.dailypaper.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.Data;

import java.util.*;

@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String[] allowedOrigin;

    // https://blog.csdn.net/qq_37896194/article/details/102833430
    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加 CORS 配置信息
        CorsConfiguration config = new CorsConfiguration();

        // config.addAllowedOrigin("*");会导致：java.lang.IllegalArgumentException: When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
        // config.addAllowedOrigin("*");

        var patterns = new ArrayList<String>();
        patterns.add("*");
        config.setAllowedOriginPatterns(patterns);

        // 1.1 允许的源
        if (allowedOrigin != null) {
            for (var i : allowedOrigin) {
                config.addAllowedOrigin(i);
            }
        }
        // 1.2 允许的请求方法
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.DELETE);
        // 1.3 允许的请求头
        config.addAllowedHeader("*");
        // 1.4 允许携带证书
        config.setAllowCredentials(true);

        // 2. 创建 UrlBasedCorsConfigurationSource 对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 3. 为特定的 URL 路径注册 CORS 配置
        source.registerCorsConfiguration("/dailypaper/**", config);

        // 4. 返回 CorsFilter
        return new CorsFilter(source);
    }
}
