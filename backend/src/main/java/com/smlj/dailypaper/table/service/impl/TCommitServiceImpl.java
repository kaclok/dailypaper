package com.smlj.dailypaper.table.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.table.dao.TCommitDao;
import com.smlj.dailypaper.table.entity.TCommit;
import com.smlj.dailypaper.table.service.TCommitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smlj.dailypaper.config.db.EDatasource;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class TCommitServiceImpl implements TCommitService {
    @Autowired
    private TCommitDao tCommitDao;

    @Override
    public int Insert(int userId, int commitDateTime, String content) {
        return tCommitDao.Insert(userId, commitDateTime, content);
    }

    @Override
    public int Insert2(TCommit tCommit) {
        return tCommitDao.Insert2(tCommit);
    }

    @Override
    public int GetLastId() {
        return tCommitDao.GetLastId();
    }

    @Override
    public TCommit FindById(int id) {
        return tCommitDao.FindById(id);
    }
}
