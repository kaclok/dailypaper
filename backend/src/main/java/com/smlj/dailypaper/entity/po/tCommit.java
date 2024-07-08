package com.smlj.dailypaper.entity.po;

import lombok.Data;

@Data
public class tCommit {
    private int id;
    private int userId;
    private long commitDateTime;
    private String content;
}
