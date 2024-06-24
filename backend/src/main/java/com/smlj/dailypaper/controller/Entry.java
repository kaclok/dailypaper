package com.smlj.dailypaper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// https://www.bilibili.com/video/BV1Lq4y1J77x?p=16&spm_id_from=pageDriver&vd_source=5c9f5bd891aee351c325bcf632b5550f 整合redis

// https://blog.csdn.net/miles067/article/details/132567377
// @RestController 是一个组合注解，它结合了 @Controller 和 @ResponseBody 注解的功能（就相当于把两个注解组合在一起）。在使用 @RestController 注解标记的类中，每个方法的返回值都会以 JSON 或 XML 的形式直接写入 HTTP 响应体中，相当于在每个方法上都添加了 @ResponseBody 注解。

// @RequestMapping是Spring MVC中用于映射web请求（如URL路径）到具体的方法上的注解。它既可以标注在类上，也可以标注在方法上。标注在类上时，表示类中的所有响应请求的方法都是以该类路径为父路径
// @GetMapping用于将HTTP get请求映射到特定处理程序的方法注解,具体来说，@GetMapping是一个组合注解，是@RequestMapping(method = RequestMethod.GET)的缩写
// @PostMapping用于将HTTP post请求映射到特定处理程序的方法注解,具体来说，@PostMapping是一个组合注解，是@RequestMapping(method = RequestMethod.POST)的缩写

// SpringBoot读取配置文件方式：https://www.bilibili.com/video/BV1Lq4y1J77x?p=10&spm_id_from=pageDriver&vd_source=5c9f5bd891aee351c325bcf632b5550f
// 1、字段@Value
// 2、Environment， autowired标记
// 3、@ConfigurationProperties， 自定义bean类类头标记， @component表示是bean类，@ConfigurationProperties一般需要指定前缀。（类名和配置的前缀绑定了，然后controller中autowired自定义bean类实例）
@RestController
public class Entry {
    // GetMapping如何截取url参数(考虑参数的可选还是必选)： https://blog.csdn.net/m0_51390969/article/details/135880395
    @GetMapping("/Hello/{id}")
    public String GetAll(@PathVariable int id) {
        return "Hello ${id}: " + id;
    }
}
