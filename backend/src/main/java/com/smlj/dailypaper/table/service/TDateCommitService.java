package com.smlj.dailypaper.table.service;

import com.smlj.dailypaper.table.entity.TDateCommit;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface TDateCommitService {
    int Insert(@Param("date") long date);

    void Update(@Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId);

    TDateCommit FindBy(@Param("date") long date);

    ArrayList<TDateCommit> GetRangeCommitsByUser(@Param("from") long from, @Param("to") long to, @Param("colName") String colName);

    ArrayList<TDateCommit> GetRangeCommits(@Param("from") long from, @Param("to") long to);
}
