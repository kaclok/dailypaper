package com.smlj.dailypaper.table.dao;

import com.smlj.dailypaper.table.entity.TDateCommit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface TDateCommitDao {
    public int Insert(@Param("date") long date);

    public void Update(@Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId);

    public TDateCommit FindBy(@Param("date") long date);

    public ArrayList<TDateCommit> GetRangeCommitsByUser(@Param("from") long from, @Param("to") long to, @Param("colName") String colName);

    public ArrayList<TDateCommit> GetRangeCommits(@Param("from") long from, @Param("to") long to);
}
