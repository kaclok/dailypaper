package com.smlj.dailypaper.services;

import com.smlj.dailypaper.entity.po.tUser;
import org.apache.ibatis.annotations.Param;

public interface UserService {
    tUser GetUserById(@Param("userId") int userId);
}
