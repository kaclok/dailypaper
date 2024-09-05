package com.smlj.dailypaper.table.entity;

import lombok.Data;

@Data
public class TCommit {
    private int id;
    private int userId;
    private long commitDateTime;
    private String content;
    private String tomorrowPlan;
}
