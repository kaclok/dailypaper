package com.smlj.dailypaper.mapper;

import com.smlj.dailypaper.entity.po.DateCommit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface DateCommitMapper {
    public int Insert(@Param("date") long date);

    public void Update(@Param("date") long date, @Param("colName") String colName, @Param("commitId") int commitId);

    public DateCommit FindBy(@Param("date") long date);
}
