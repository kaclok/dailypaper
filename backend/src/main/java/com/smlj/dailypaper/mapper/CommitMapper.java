package com.smlj.dailypaper.mapper;

import com.smlj.dailypaper.entity.po.Commit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CommitMapper {
    public int Insert(@Param("userId") int userId, @Param("commitDateTime") int commitDateTime, @Param("content") String content);

    public int Insert2(Commit commit);

    public int GetLastId();

    public Commit FindById(@Param("id") int id);
}
