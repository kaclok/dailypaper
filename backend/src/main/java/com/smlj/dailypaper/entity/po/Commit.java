package com.smlj.dailypaper.entity.po;

import lombok.Data;

@Data
public class Commit {
    public int id;
    public int userId;
    public long commitDateTime;
    public String content;
}
