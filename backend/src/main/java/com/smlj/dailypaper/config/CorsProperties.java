package com.smlj.dailypaper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.Data;

@Data
@Configuration
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private String allowedOrigin;

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加 CORS 配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 1.1 允许的源
        config.addAllowedOrigin(allowedOrigin);
        // 1.2 允许的请求方法
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
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
