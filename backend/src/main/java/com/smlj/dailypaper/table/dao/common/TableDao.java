package com.smlj.dailypaper.table.dao.common;

import com.smlj.dailypaper.config.db.TableColumn;
import com.smlj.dailypaper.config.db.TableIndex;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface TableDao<Tkey, TLine> {
    String OwnerDB();

    int Exist(@Param("tableName") String tableName);

    int Count(@Param("tableName") String tableName);

    void DeleteByField(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("fieldValue") Tkey fieldValue);

    TLine SelectByKey(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("fieldValue") Tkey fieldValue);

    ArrayList<TLine> SelectByKeys(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("list") ArrayList<Tkey> list);

    ArrayList<Integer> FieldList(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("distinct") boolean distinct);

    void Drop(@Param("tableName") String tableName);

    ArrayList<TableIndex> ShowIndex(@Param("tableName") String tableName);

    ArrayList<TableColumn> ShowColumn(@Param("tableName") String tableName);

    ArrayList<String> ShowComment(@Param("tableName") String tableName);
}
