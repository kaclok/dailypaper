package com.smlj.dailypaper.table.service;

import com.smlj.dailypaper.table.entity.TUser;
import org.apache.ibatis.annotations.Param;

public interface TUserService {
    TUser GetUserById(@Param("userId") int userId);
}
