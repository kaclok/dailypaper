package com.smlj.dailypaper.table.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.smlj.dailypaper.config.db.EDatasource;
import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.dao.TUserDao;
import com.smlj.dailypaper.table.service.TUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DS(EDatasource.dailypaper)
public class TUserServiceImpl implements TUserService {
    @Autowired
    private TUserDao tUserDao;

    @Override
    public TUser GetUserById(int userId) {
        return tUserDao.GetUserById(userId);
    }
}
