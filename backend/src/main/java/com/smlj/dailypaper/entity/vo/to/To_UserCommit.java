package com.smlj.dailypaper.entity.vo.to;

import lombok.Data;

import java.io.Serializable;

@Data
public class To_UserCommit implements Serializable, Comparable<To_UserCommit> {
    private int userId;
    private String name;
    private String content;
    private long time;

    @Override
    public int compareTo(To_UserCommit toUserCommit) {
        return this.userId - toUserCommit.userId;
    }
}
