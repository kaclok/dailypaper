package com.smlj.dailypaper.table.service;

import com.smlj.dailypaper.table.entity.TCommit;
import com.smlj.dailypaper.table.entity.TWeekPlanCommit;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface TWeekPlanCommitService {
    public void Create(@Param("tableName") String tableName);

    void InsertOutKey(@Param("tableName") String tableName, @Param("one") TWeekPlanCommit one);

    TWeekPlanCommit FindById(@Param("tableName") String tableName, @Param("id") int id);
}
