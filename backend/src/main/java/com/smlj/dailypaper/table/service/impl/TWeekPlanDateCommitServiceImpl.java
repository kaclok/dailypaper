package com.smlj.dailypaper.table.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.config.db.EDatasource;
import com.smlj.dailypaper.table.dao.TWeekPlanDateCommitDao;
import com.smlj.dailypaper.table.service.TWeekPlanDateCommitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class TWeekPlanDateCommitServiceImpl implements TWeekPlanDateCommitService {
    @Autowired
    private TWeekPlanDateCommitDao tDateCommitDao;

    @Override
    public void Create(@Param("tableName") String tableName, @Param("list") ArrayList<String> list) {
        tDateCommitDao.Create(tableName, list);
    }

    @Override
    public void InsertEmpty(String tableName, long date) {
        tDateCommitDao.InsertEmpty(tableName, date);
    }

    @Override
    public void Update(String tableName, long date, String colName, int commitId) {
        tDateCommitDao.Update(tableName, date, colName, commitId);
    }

    @Override
    public HashMap<String, Object> FindBy(String tableName, long date) {
        return tDateCommitDao.FindBy(tableName, date);
    }

    @Override
    public ArrayList<HashMap<String, Object>> GetRangeCommits(String tableName, long from, long to) {
        return tDateCommitDao.GetRangeCommits(tableName, from, to);
    }
}
