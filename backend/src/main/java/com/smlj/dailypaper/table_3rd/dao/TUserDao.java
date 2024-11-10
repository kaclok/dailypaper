package com.smlj.dailypaper.table_3rd.dao;

import com.smlj.dailypaper.table_3rd.entity.TUser;

import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
@Repository("train_TUserDao")
public interface TUserDao {
    ArrayList<TUser> selectByAccount(@Param("userAccount") String userAccount);

    String getDepartmentCode(@Param("userAccount") String userAccount);

    String getDepartmentName(@Param("userAccount") String userAccount);
}

