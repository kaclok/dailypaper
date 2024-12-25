package com.smlj.dailypaper.proto.to;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class To_DateCommit implements Serializable {
    private int total = 16;
    private long date = 0;
    private String departmentId;
    private String departmentName = null;
    private boolean curUserIsLeader = false;

    private ArrayList<To_UserCommit> commits = new ArrayList<To_UserCommit>(16);
}
