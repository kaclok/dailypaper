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
    public int Insert(long date) {
        return tDateCommitDao.Insert(date);
    }

    @Override
    public void Update(long date, String colName, int commitId) {
        tDateCommitDao.Update(date, colName, commitId);
    }

    @Override
    public TDateCommit FindBy(long date) {
        return tDateCommitDao.FindBy(date);
    }

    @Override
    public ArrayList<TDateCommit> GetRangeCommitsByUser(long from, long to, String colName) {
        return tDateCommitDao.GetRangeCommitsByUser(from, to, colName);
    }

    @Override
    public ArrayList<TDateCommit> GetRangeCommits(long from, long to) {
        return tDateCommitDao.GetRangeCommits(from, to);
    }
}
