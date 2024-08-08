package com.smlj.dailypaper.mapper;

import com.smlj.dailypaper.entity.po.tDateCommit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface DateCommitMapper {
    public int Insert(@Param("date") long date);

    public void Update(@Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId);

    public tDateCommit FindBy(@Param("date") long date);

    public ArrayList<tDateCommit> GetRangeCommitsByUser(@Param("from") long from, @Param("to") long to, @Param("colName") String colName);

    public ArrayList<tDateCommit> GetRangeCommits(@Param("from") long from, @Param("to") long to);
}
