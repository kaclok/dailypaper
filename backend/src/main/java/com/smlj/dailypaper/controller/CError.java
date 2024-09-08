package com.smlj.dailypaper.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// 全局异常处理器
// 处理404、错误等情况， 可以利用Thymeleaf返回404.html或者error.html
@Controller
@Slf4j
public class CError implements ErrorController {
    // 测试服务器是否启动成功
    @GetMapping("/")
    private String defaultIndex(Model model) {
        /* 测试异常被捕获
            Integer i = 10;
            i = i / 0;
        */
        // 模版引擎Thymeleaf
        // https://blog.csdn.net/ymaini/article/details/85269200
        // https://blog.csdn.net/Lzy410992/article/details/115371017
        model.addAttribute("msg", "Hello, Thymeleaf - 1!");
        // 默认html文件存储在templates下
        return "views/index"; // 返回的是html的路径/文件名
    }

    /*
        当 Spring Boot 接收到不存在的请求连接时，默认情况下会按照以下步骤执行：
        首先，Spring Boot 的 DispatcherServlet 会接收到这个请求。
        然后，它会遍历已配置的所有控制器（Controller）及其方法上的 @RequestMapping 注解，尝试匹配当前请求的 URL 和 HTTP 方法。
        如果没有找到匹配的处理方法，Spring Boot 会检查是否配置了自定义的错误处理机制。
        如果没有自定义的错误处理，默认情况下会返回 404（Not Found）状态码。
        如果您自定义了错误处理，例如通过实现 ErrorController 或使用 @ControllerAdvice 注解来处理全局异常，那么在没有匹配到有效请求处理方法时，将执行您自定义的错误处理逻辑。

        @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)全局异常捕获会优先捕获异常，如果没有全局异常捕获，那么controller内部的异常也会被/error
        捕获到，从而通过html信息输出
    */

    // 处理404 或者 代码异常 的展示
    @RequestMapping("/error")
    public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
        log.info("statusCode:{}", response.getStatus());
        if (response.getStatus() == 404) {
            return "views/404";
        } else {
            Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");
            model.addAttribute("exceptionMessage", exception.toString());
            return "views/error";
        }
    }

    // 全局异常捕获
    //    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    //    public void exception(Exception e) {
    //        log.info("-------exception begin-------");
    //        e.printStackTrace();
    //        log.info("-------exception   end-------");
    //    }
}
