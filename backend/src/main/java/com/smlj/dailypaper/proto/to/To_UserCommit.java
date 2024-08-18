package com.smlj.dailypaper.proto.to;

import lombok.Data;

import java.io.Serializable;

@Data
public class To_UserCommit implements Serializable, Comparable<To_UserCommit> {
    private int userId = 0;
    private String name = null;
    private String account = null;
    private String content = null;
    private long time = 0;

    @Override
    public int compareTo(To_UserCommit toUserCommit) {
        return this.userId - toUserCommit.userId;
    }
}
