package com.smlj.dailypaper.table.service;

import org.apache.ibatis.annotations.Param;
import com.smlj.dailypaper.table.entity.TCommit;

public interface TCommitService {
    int Insert(@Param("userId") int userId, @Param("commitDateTime") int commitDateTime, @Param("content") String content);

    int Insert2(TCommit tCommit);

    int GetLastId();

    TCommit FindById(@Param("id") int id);
}
