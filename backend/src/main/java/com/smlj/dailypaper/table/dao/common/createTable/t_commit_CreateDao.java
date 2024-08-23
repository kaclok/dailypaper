package com.smlj.dailypaper.table.dao.common.createTable;

import org.apache.ibatis.annotations.Param;

public interface t_commit_CreateDao {
    void Create(@Param("tableName") String tableName);
}
