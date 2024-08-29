package com.smlj.dailypaper.table.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.table.dao.TCommitDao;
import com.smlj.dailypaper.table.entity.TCommit;
import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.service.TCommitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smlj.dailypaper.config.db.EDatasource;

import java.util.ArrayList;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class TCommitServiceImpl implements TCommitService {
    @Autowired
    private TCommitDao tCommitDao;

    @Override
    public int Insert(String tableName, int userId, int commitDateTime, String content) {
        return tCommitDao.Insert(tableName, userId, commitDateTime, content);
    }

    @Override
    public void InsertOutKey(String tableName, TCommit one) {
        tCommitDao.InsertOutKey(tableName, one);
    }

    @Override
    public void InsertBatch(String tableName, ArrayList<TCommit> list) {
        tCommitDao.InsertBatch(tableName, list);
    }

    @Override
    public TCommit FindById(String tableName, int id) {
        return tCommitDao.FindById(tableName, id);
    }
}
