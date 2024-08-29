package com.smlj.dailypaper.table.dao;

import com.smlj.dailypaper.table.dao.common.TableCreateDao;
import com.smlj.dailypaper.table.entity.TUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface TUserDao extends TableCreateDao {
    // 测试非xml配置的模式进行数据库访问
    @Select("select * from t_user")
    public ArrayList<TUser> FindAll();

    public TUser GetUserById(@Param("userId") int userId);
}
