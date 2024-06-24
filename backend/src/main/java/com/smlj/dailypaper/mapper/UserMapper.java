package com.smlj.dailypaper.mapper;

import com.smlj.dailypaper.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {
    @Select("select * from t_user")
    public List<User> FindAll();

    public List<User> GetAll();
}
