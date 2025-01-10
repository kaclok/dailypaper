package com.smlj.dailypaper.proto.to;

import lombok.Data;

import java.io.Serializable;

@Data
public class To_WeekPlan implements Serializable {
    private String userId; // 集团工号
    private String name;
    private String account = null; // 子公司账号
    private String dutyPerson = null;
    private String content = null;
    private boolean isLeader;
    private long finishTime;
    private String comment = null;
}
