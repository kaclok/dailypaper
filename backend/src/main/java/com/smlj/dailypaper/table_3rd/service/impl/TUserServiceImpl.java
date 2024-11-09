package com.smlj.dailypaper.table_3rd.service.impl;

import com.smlj.dailypaper.table_3rd.entity.TUser;
import com.smlj.dailypaper.table_3rd.dao.TUserDao;
import com.smlj.dailypaper.table_3rd.service.TUserService;
import com.smlj.dailypaper.config.db.EDatasource;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;

/**
 * (TUser)表服务实现类
 *
 * @author Cui
 * @since 2024-08-19 08:43:23
 */
@Slf4j
@DS(EDatasource.train)
@Service("jtmenhu_TUserService")
public class TUserServiceImpl implements TUserService {
    @Autowired
    @Qualifier("jtmenhu_TUserDao")
    private TUserDao tUserDao;

    @Override
    public ArrayList<TUser> selectByAccount(@Param("userAccount") String userAccount) {
        return tUserDao.selectByAccount(userAccount);
    }

    @Override
    public String getDepartmentCode(@Param("userAccount") String userAccount) {
        return tUserDao.getDepartmentCode(userAccount);
    }

    @Override
    public String getDepartmentName(@Param("userAccount") String userAccount) {
        return tUserDao.getDepartmentName(userAccount);
    }
}
