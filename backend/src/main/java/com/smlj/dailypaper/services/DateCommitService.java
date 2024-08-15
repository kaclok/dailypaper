package com.smlj.dailypaper.services;

import com.smlj.dailypaper.entity.po.tDateCommit;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface DateCommitService {
    int Insert(@Param("date") long date);

    void Update(@Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId);

    tDateCommit FindBy(@Param("date") long date);

    ArrayList<tDateCommit> GetRangeCommitsByUser(@Param("from") long from, @Param("to") long to, @Param("colName") String colName);

    ArrayList<tDateCommit> GetRangeCommits(@Param("from") long from, @Param("to") long to);
}
