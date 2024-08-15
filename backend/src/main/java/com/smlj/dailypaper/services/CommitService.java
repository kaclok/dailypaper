package com.smlj.dailypaper.services;

import com.smlj.dailypaper.entity.po.tCommit;
import org.apache.ibatis.annotations.Param;

public interface CommitService {
    int Insert(@Param("userId") int userId, @Param("commitDateTime") int commitDateTime, @Param("content") String content);

    int Insert2(tCommit tCommit);

    int GetLastId();

    tCommit FindById(@Param("id") int id);
}
