package com.smlj.dailypaper;

import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.dao.TCommitDao;
import com.smlj.dailypaper.table.dao.TDateCommitDao;
import com.smlj.dailypaper.table.dao.TUserDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DailypaperApplicationTests {
    @Autowired
    private TUserDao tUserDao;

    @Test
    void testFindAll() {
        List<TUser> list = tUserDao.FindAll();
        System.out.println(list);
    }
}
