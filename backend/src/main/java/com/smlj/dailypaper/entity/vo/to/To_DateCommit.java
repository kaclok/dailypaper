package com.smlj.dailypaper.entity.vo.to;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class To_DateCommit implements Serializable {
    private int total = 13;
    private long date;
    private ArrayList<To_UserCommit> commits = new ArrayList<To_UserCommit>(13);
}
