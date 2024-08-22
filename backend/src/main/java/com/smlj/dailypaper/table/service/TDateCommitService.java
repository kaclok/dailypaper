package com.smlj.dailypaper.table.service;

import com.smlj.dailypaper.table.entity.TDateCommit;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface TDateCommitService {
    int Insert(@Param("date") long date, @Param("tName") String tName);

    void Update(@Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId, @Param("tName") String tName);

    TDateCommit FindBy(@Param("date") long date, @Param("tName") String tName);

    ArrayList<TDateCommit> GetRangeCommitsByUser(@Param("from") long from, @Param("to") long to, @Param("colName") String colName, @Param("tName") String tName);

    ArrayList<TDateCommit> GetRangeCommits(@Param("from") long from, @Param("to") long to, @Param("tName") String tName);
}
