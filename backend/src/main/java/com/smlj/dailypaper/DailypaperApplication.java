package com.smlj.dailypaper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableWebMvc
// 导入Gson https://springdoc.cn/spring-boot-gson/
// 有了 多数据源 和动态台数据源 需要屏蔽默认的数据源配置形式
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy(exposeProxy = true) // http://www.voidcn.com/article/p-zddcuyii-bpt.html
@MapperScan({"com.smlj.dailypaper.table.dao", "com.smlj.dailypaper.table.dao.common", "com.smlj.dailypaper.table.dao.common.createTable", "com.smlj.dailypaper.table_3rd.dao"})
public class DailypaperApplication {
    public static void main(String[] args) {
        SpringApplication.run(DailypaperApplication.class, args);
    }
}
