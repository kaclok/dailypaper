package com.smlj.dailypaper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

// 导入Gson https://springdoc.cn/spring-boot-gson/
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
@EnableWebMvc
public class DailypaperApplication {
    public static void main(String[] args) {
        SpringApplication.run(DailypaperApplication.class, args);
    }
}
