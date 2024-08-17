package com.smlj.dailypaper.entity.vo.to;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class To_DateCommit implements Serializable {
    private int total = 16;
    private long date = 0;
    private int departmentId = 0;
    private String departmentName = null;
    private ArrayList<To_UserCommit> commits = new ArrayList<To_UserCommit>(16);
}
