package com.smlj.dailypaper.entity.po;

import lombok.Data;

// https://blog.csdn.net/Alex_81D/article/details/85337730
// @Data特性可以帮助自己写getter、setter等函数
@Data
public class User {
    private int id;
    private String name;
    private int age;
    private String identity;
}
