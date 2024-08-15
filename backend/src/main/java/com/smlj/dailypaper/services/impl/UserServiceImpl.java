package com.smlj.dailypaper.services.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.config.db.EDatasource;
import com.smlj.dailypaper.entity.po.tUser;
import com.smlj.dailypaper.mapper.UserMapper;
import com.smlj.dailypaper.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public tUser GetUserById(int userId) {
        return userMapper.GetUserById(userId);
    }
}
