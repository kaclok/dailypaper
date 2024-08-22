package com.smlj.dailypaper.table.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.table.entity.TDateCommit;
import com.smlj.dailypaper.table.dao.TDateCommitDao;
import com.smlj.dailypaper.table.service.TDateCommitService;
import com.smlj.dailypaper.config.db.EDatasource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class TDateCommitServiceImpl implements TDateCommitService {
    @Autowired
    private TDateCommitDao tDateCommitDao;

    @Override
    public int Insert(long date, String tName) {
        return tDateCommitDao.Insert(date, tName);
    }

    @Override
    public void Update(long date, String colName, int commitId, String tName) {
        tDateCommitDao.Update(date, colName, commitId, tName);
    }

    @Override
    public TDateCommit FindBy(long date, String tName) {
        return tDateCommitDao.FindBy(date, tName);
    }

    @Override
    public ArrayList<TDateCommit> GetRangeCommitsByUser(long from, long to, String colName, String tName) {
        return tDateCommitDao.GetRangeCommitsByUser(from, to, colName, tName);
    }

    @Override
    public ArrayList<TDateCommit> GetRangeCommits(long from, long to, String tName) {
        return tDateCommitDao.GetRangeCommits(from, to, tName);
    }
}
