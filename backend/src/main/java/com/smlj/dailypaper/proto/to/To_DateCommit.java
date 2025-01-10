package com.smlj.dailypaper.proto.to;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class To_DateCommit implements Serializable {
    private long date = 0;
    private String departmentId;
    private String departmentName = null;
    private boolean curUserIsLeader = false;
    private ArrayList<InnerUser> people = new ArrayList<>();

    private ArrayList<To_UserCommit> dailyPlan = new ArrayList<To_UserCommit>();
    private ArrayList<To_WeekPlan> weeklyPlan = new ArrayList<To_WeekPlan>();

    @Data
    public static class InnerUser {
        private String userId;
        private String userAccount;
        private String userName;
    }
}
