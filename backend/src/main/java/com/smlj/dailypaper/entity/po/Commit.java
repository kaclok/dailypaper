package com.smlj.dailypaper.entity.po;

import lombok.Data;

@Data
public class Commit {
    private int id;
    private int userId;
    private long commitDateTime;
    private String content;
}
