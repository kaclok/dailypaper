package com.smlj.dailypaper.table_3rd.service;

import com.smlj.dailypaper.table_3rd.entity.TUser;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * (TUser)表服务接口
 *
 * @author Cui
 * @since 2024-08-19 08:29:19
 */
public interface TUserService {
    ArrayList<TUser> selectByAccount(@Param("userAccount") String userAccount);

    String getDepartmentCode(@Param("userAccount") String userAccount);

    String getDepartmentName(@Param("userAccount") String userAccount);
}
