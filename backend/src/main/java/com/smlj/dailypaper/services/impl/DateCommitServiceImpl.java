package com.smlj.dailypaper.services.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.entity.po.tDateCommit;
import com.smlj.dailypaper.mapper.DateCommitMapper;
import com.smlj.dailypaper.services.DateCommitService;
import com.smlj.dailypaper.config.db.EDatasource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class DateCommitServiceImpl implements DateCommitService {
    @Autowired
    private DateCommitMapper dateCommitMapper;

    @Override
    public int Insert(long date) {
        return dateCommitMapper.Insert(date);
    }

    @Override
    public void Update(long date, String colName, int commitId) {
        dateCommitMapper.Update(date, colName, commitId);
    }

    @Override
    public tDateCommit FindBy(long date) {
        return dateCommitMapper.FindBy(date);
    }

    @Override
    public ArrayList<tDateCommit> GetRangeCommitsByUser(long from, long to, String colName) {
        return dateCommitMapper.GetRangeCommitsByUser(from, to, colName);
    }

    @Override
    public ArrayList<tDateCommit> GetRangeCommits(long from, long to) {
        return dateCommitMapper.GetRangeCommits(from, to);
    }
}
