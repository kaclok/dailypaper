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
@Service("train_TUserService")
public class TUserServiceImpl implements TUserService {
    @Autowired
    @Qualifier("train_TUserDao")
    private TUserDao tUserDao;

    @Override
    public ArrayList<TUser> selectMembersByAccount(@Param("userId") String userId) {
        return tUserDao.selectMembersByAccount(userId);
    }

    @Override
    public TUser getLeader(@Param("deptCode") String deptCode) {
        return tUserDao.getLeader(deptCode);
    }

    @Override
    public String getDepartmentCode(@Param("userId") String userId) {
        return tUserDao.getDepartmentCode(userId);
    }

    @Override
    public String getDepartmentName(@Param("deptCode") String deptCode) {
        return tUserDao.getDepartmentName(deptCode);
    }
}
