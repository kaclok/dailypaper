package com.smlj.dailypaper.table.entity;

import lombok.Data;

@Data
public class TCommit {
    private int id;
    private String userId; // 集团工号
    private long commitDateTime;
    private String content;
    private String tomorrowPlan;
    private String tomorrowArrangement;
}
