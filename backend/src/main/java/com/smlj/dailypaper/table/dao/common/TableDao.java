package com.smlj.dailypaper.table.dao.common;

import com.smlj.dailypaper.config.db.TableColumn;
import com.smlj.dailypaper.config.db.TableIndex;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface TableDao {
    String OwnerDB();

    int Exist(@Param("tableName") String tableName);

    int Count(@Param("tableName") String tableName);

    ArrayList<Integer> KeyList(@Param("tableName") String tableName, @Param("keyName") String keyName);

    void Drop(@Param("tableName") String tableName);

    ArrayList<TableIndex> ShowIndex(@Param("tableName") String tableName);

    ArrayList<TableColumn> ShowColumn(@Param("tableName") String tableName);

    ArrayList<String> ShowComment(@Param("tableName") String tableName);
}
