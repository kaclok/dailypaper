package com.smlj.dailypaper.table_3rd.dao;

import com.smlj.dailypaper.table_3rd.entity.TUser;

import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
@Repository("train_TUserDao")
public interface TUserDao {
    ArrayList<TUser> selectMembersByAccount(@Param("userId") String userId);

    TUser getLeader(@Param("deptCode") String deptCode);

    String getDepartmentCode(@Param("userId") String userId);

    String getDepartmentName(@Param("deptCode") String deptCode);
}

