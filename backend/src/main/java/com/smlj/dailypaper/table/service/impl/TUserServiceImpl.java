package com.smlj.dailypaper.table.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.config.db.EDatasource;
import com.smlj.dailypaper.table.dao.TUserDao;
import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.service.TUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class TUserServiceImpl implements TUserService {
    @Autowired
    private TUserDao tUserDao;

    @Override
    public TUser GetUserById(String tableName, String userCard) {
        return tUserDao.GetUserById(tableName, userCard);
    }

    public void Create(String tableName) {
        tUserDao.Create(tableName);
    }

    public int Count(String tableName) {
        return tUserDao.Count(tableName);
    }

    @Override
    public void Insert(String tableName, TUser one) {
        tUserDao.Insert(tableName, one);
    }

    @Override
    public void InsertBatch(String tableName, ArrayList<TUser> list) {
        tUserDao.InsertBatch(tableName, list);
    }

    @Override
    public ArrayList<TUser> FindAll(String tableName) {
        return tUserDao.FindAll(tableName);
    }
}
