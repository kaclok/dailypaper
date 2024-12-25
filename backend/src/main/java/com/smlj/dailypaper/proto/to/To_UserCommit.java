package com.smlj.dailypaper.proto.to;

import lombok.Data;

import java.io.Serializable;

@Data
public class To_UserCommit implements Serializable {
    private String userId; // 集团工号
    private String name = null;
    private String account = null; // 子公司账号
    private String content = null;
    private String tomorrowPlan = null;
    private String tomorrowArrangement = null;
    private boolean isLeader;
    private long time = 0;
}
