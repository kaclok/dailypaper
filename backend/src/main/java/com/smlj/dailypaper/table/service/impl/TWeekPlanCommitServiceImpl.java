package com.smlj.dailypaper.table.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.config.db.EDatasource;
import com.smlj.dailypaper.table.dao.TCommitDao;
import com.smlj.dailypaper.table.dao.TWeekPlanCommitDao;
import com.smlj.dailypaper.table.entity.TCommit;
import com.smlj.dailypaper.table.entity.TWeekPlanCommit;
import com.smlj.dailypaper.table.service.TCommitService;
import com.smlj.dailypaper.table.service.TWeekPlanCommitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class TWeekPlanCommitServiceImpl implements TWeekPlanCommitService {
    @Autowired
    private TWeekPlanCommitDao tCommitDao;

    public void Create(String tableName) {
        tCommitDao.Create(tableName);
    }

    @Override
    public void InsertOutKey(String tableName, TWeekPlanCommit one) {
        tCommitDao.InsertOutKey(tableName, one);
    }

    @Override
    public TWeekPlanCommit FindById(String tableName, int id) {
        return tCommitDao.FindById(tableName, id);
    }
}
