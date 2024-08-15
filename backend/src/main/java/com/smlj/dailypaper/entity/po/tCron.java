package com.smlj.dailypaper.entity.po;

import lombok.Data;

// https://blog.csdn.net/Alex_81D/article/details/85337730
// https://cron.ciding.cc/
// https://blog.csdn.net/qianlixiaomage/article/details/106599951
// https://www.runoob.com/w3cnote/linux-crontab-tasks.html
// @Data特性可以帮助自己写getter、setter等函数
@Data
public class tCron {
    private int id;
    private String goal;
    private String cron;
}
