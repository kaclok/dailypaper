package com.smlj.dailypaper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

// 导入Gson https://springdoc.cn/spring-boot-gson/
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class DailypaperApplication {
    public static void main(String[] args) {
        SpringApplication.run(DailypaperApplication.class, args);
    }
}
