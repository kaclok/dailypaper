package com.smlj.dailypaper.table.service;

import com.smlj.dailypaper.table.entity.TUser;
import org.apache.ibatis.annotations.Param;
import com.smlj.dailypaper.table.entity.TCommit;

import java.util.ArrayList;

public interface TCommitService {
    public void Create(@Param("tableName") String tableName);

    void InsertOutKey(@Param("tableName") String tableName, @Param("one") TCommit one);

    void InsertBatch(@Param("tableName") String tableName, @Param("list") ArrayList<TCommit> list);

    TCommit FindById(@Param("tableName") String tableName, @Param("id") int id);
}
