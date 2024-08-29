package com.smlj.dailypaper.table.dao;

import com.smlj.dailypaper.table.dao.common.TableCreateDao;
import com.smlj.dailypaper.table.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.smlj.dailypaper.table.entity.TCommit;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface TCommitDao extends TableCreateDao {
    public int Insert(@Param("tableName") String tableName, @Param("userId") int userId, @Param("commitDateTime") int commitDateTime, @Param("content") String content);

    public void InsertOutKey(@Param("tableName") String tableName, @Param("one") TCommit one);

    public void InsertBatch(@Param("tableName") String tableName, @Param("list") ArrayList<TCommit> list);

    public int GetLastId(@Param("tableName") String tableName);

    public TCommit FindById(@Param("tableName") String tableName, @Param("id") int id);
}
