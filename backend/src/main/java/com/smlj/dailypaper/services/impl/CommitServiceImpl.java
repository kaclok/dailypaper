package com.smlj.dailypaper.services.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.entity.po.tCommit;
import com.smlj.dailypaper.mapper.CommitMapper;
import com.smlj.dailypaper.services.CommitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smlj.dailypaper.config.db.EDatasource;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class CommitServiceImpl implements CommitService {
    @Autowired
    private CommitMapper commitMapper;

    @Override
    public int Insert(int userId, int commitDateTime, String content) {
        return commitMapper.Insert(userId, commitDateTime, content);
    }

    @Override
    public int Insert2(tCommit tCommit) {
        return commitMapper.Insert2(tCommit);
    }

    @Override
    public int GetLastId() {
        return commitMapper.GetLastId();
    }

    @Override
    public tCommit FindById(int id) {
        return commitMapper.FindById(id);
    }
}
