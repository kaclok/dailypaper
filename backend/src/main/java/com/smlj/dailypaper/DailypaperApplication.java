package com.smlj.dailypaper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableWebMvc
// 导入Gson https://springdoc.cn/spring-boot-gson/
// 有了 多数据源 和动态台数据源 需要屏蔽默认的数据源配置形式
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy(exposeProxy = true) // http://www.voidcn.com/article/p-zddcuyii-bpt.html
@MapperScan({"com.smlj.dailypaper.table.dao", "com.smlj.dailypaper.table.dao.common", "com.smlj.dailypaper.table_3rd.dao"})

// https://blog.csdn.net/ymaini/article/details/85269200
// 注解是@Controller不是@RestController
@Controller
public class DailypaperApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DailypaperApplication.class);
        // https://mp.weixin.qq.com/s/q8_KWBD-zS9OIKd8oJ4Eag
        // 关闭banner
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    // 测试服务器是否启动成功
    @GetMapping("/")
    private String index(Model model) {
        // 模版引擎Thymeleaf
        // https://blog.csdn.net/ymaini/article/details/85269200
        // https://blog.csdn.net/Lzy410992/article/details/115371017
        model.addAttribute("msg", "Hello, Thymeleaf - 1!");
        // 默认html文件存储在templates下
        return "views/index"; // 返回的是html的路径/文件名
    }
}
