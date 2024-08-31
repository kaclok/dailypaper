package com.smlj.dailypaper.table.service;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashMap;

public interface TDateCommitService {
    void Create(@Param("tableName") String tableName, @Param("list") ArrayList<Integer> list);

    void InsertEmpty(@Param("tableName") String tableName, @Param("date") long date);

    void Update(@Param("tableName") String tableName, @Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId);

    HashMap<String, Object> FindBy(@Param("tableName") String tableName, @Param("date") long date);

    ArrayList<HashMap<String, Long>> GetRangeCommitsByUser(@Param("tableName") String tableName, @Param("from") long from, @Param("to") long to, @Param("colName") String colName);

    ArrayList<HashMap<String, Object>> GetRangeCommits(@Param("tableName") String tableName, @Param("from") long from, @Param("to") long to);
}
