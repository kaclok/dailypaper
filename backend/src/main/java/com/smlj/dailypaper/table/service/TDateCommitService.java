package com.smlj.dailypaper.table.service;

import com.smlj.dailypaper.table.entity.TDateCommit;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface TDateCommitService {
    void Create(@Param("tableName") String tableName, @Param("list") ArrayList<Integer> list);

    void InsertEmpty(@Param("tableName") String tableName, @Param("date") long date);

    void Update(@Param("tableName") String tableName, @Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId);

    TDateCommit FindBy(@Param("tableName") String tableName, @Param("date") long date);

    ArrayList<TDateCommit> GetRangeCommitsByUser(@Param("tableName") String tableName, @Param("from") long from, @Param("to") long to, @Param("colName") String colName);

    ArrayList<TDateCommit> GetRangeCommits(@Param("tableName") String tableName, @Param("from") long from, @Param("to") long to);
}
