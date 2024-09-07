package com.smlj.dailypaper.table.dao.common;

import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface KVTableDao<Tkey, TLine> extends KTableDao<Tkey> {
    TLine SelectByKey(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("fieldValue") Tkey fieldValue);

    ArrayList<TLine> SelectByKeys(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("list") ArrayList<Tkey> list);
}
