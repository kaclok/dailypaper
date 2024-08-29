package com.smlj.dailypaper.table.service;

import com.smlj.dailypaper.table.entity.TUser;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

public interface TUserService {
    TUser GetUserById(@Param("tableName") String tableName, @Param("userId") int userId);

    public void Create(@Param("tableName") String tableName);

    public void Insert(@Param("tableName") String tableName, @Param("one") TUser one);

    public void InsertBatch(@Param("tableName") String tableName, @Param("list") ArrayList<TUser> list);
}
