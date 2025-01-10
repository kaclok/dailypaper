package com.smlj.dailypaper.table.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TWeekPlanCommit {
    private int id;
    private String userId; // 集团工号
    private long commitDateTime;
    private String content;
    private Date finishTime;
    private String comment;
}
