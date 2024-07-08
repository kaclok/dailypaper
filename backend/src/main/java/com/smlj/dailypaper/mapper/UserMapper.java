package com.smlj.dailypaper.mapper;

import com.smlj.dailypaper.entity.po.tUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface UserMapper {
    // 测试非xml配置的模式进行数据库访问
    @Select("select * from t_user")
    public ArrayList<tUser> FindAll();

    public tUser GetUserById(@Param("userId") int userId);
}
