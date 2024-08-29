package com.smlj.dailypaper.table.dao.common;

import org.apache.ibatis.annotations.Param;

public interface TableCreateDao {
    void Create(@Param("tableName") String tableName);
}
