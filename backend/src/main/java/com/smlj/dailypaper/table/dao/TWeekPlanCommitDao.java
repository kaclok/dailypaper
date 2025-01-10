package com.smlj.dailypaper.table.dao;

import com.smlj.dailypaper.table.dao.common.TableCreateDao;
import com.smlj.dailypaper.table.entity.TCommit;
import com.smlj.dailypaper.table.entity.TWeekPlanCommit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface TWeekPlanCommitDao extends TableCreateDao {
    public void InsertOutKey(@Param("tableName") String tableName, @Param("one") TWeekPlanCommit one);

    public TWeekPlanCommit FindById(@Param("tableName") String tableName, @Param("id") int id);
}
