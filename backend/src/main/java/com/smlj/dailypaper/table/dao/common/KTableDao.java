package com.smlj.dailypaper.table.dao.common;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface KTableDao<Tkey> extends TableDao {
    void DeleteByField(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("fieldValue") Tkey fieldValue);

    // ArrayList<Tkey> FieldList(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("distinct") boolean distinct);
}
