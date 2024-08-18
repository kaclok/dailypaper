package com.smlj.dailypaper.table.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.smlj.dailypaper.table.entity.TCommit;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TCommitDao {
    public int Insert(@Param("userId") int userId, @Param("commitDateTime") int commitDateTime, @Param("content") String content);

    public int Insert2(TCommit tCommit);

    public int GetLastId();

    public TCommit FindById(@Param("id") int id);
}
