package com.smlj.dailypaper.table.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
@Mapper
public interface TDateCommitDao {
    public void Create(@Param("tableName") String tableName, @Param("list") ArrayList<Integer> list);

    public void InsertEmpty(@Param("tableName") String tableName, @Param("date") long date);

    public void Update(@Param("tableName") String tableName, @Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId);

    // 就是因为datecommit表中有value为string的dateFormat这列，否则可以为：HashMap<String, Long>
    public HashMap<String, Object> FindBy(@Param("tableName") String tableName, @Param("date") long date);

    public ArrayList<HashMap<String, Long>> GetRangeCommitsByUser(@Param("tableName") String tableName, @Param("from") long from, @Param("to") long to, @Param("colName") String colName);

    // 就是因为datecommit表中有value为string的dateFormat这列，否则可以为：HashMap<String, Long>
    public ArrayList<HashMap<String, Object>> GetRangeCommits(@Param("tableName") String tableName, @Param("from") long from, @Param("to") long to);
}
