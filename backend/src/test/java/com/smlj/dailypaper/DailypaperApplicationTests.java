package com.smlj.dailypaper;

import com.smlj.dailypaper.table.dao.TCommitDao;
import com.smlj.dailypaper.table.dao.TDateCommitDao;
import com.smlj.dailypaper.table.dao.TUserDao;
import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.table.entity.TUser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {DailypaperApplication.class})
class DailypaperApplicationTests {
    @Autowired
    private TUserDao tUserDao;

    @Autowired
    private TableDao tableDao;

    @Autowired
    private TCommitDao commit_createDao;

    @Autowired
    private TDateCommitDao datecommit_createDao;

    @Autowired
    private com.smlj.dailypaper.table_3rd.service.TUserService jt_userService;

    @Test
    void testFindAll() {
        List<TUser> list = tUserDao.FindAll();
        System.out.println(list);
    }

    @Test
    void ExistTable() {
        int rlt = tableDao.Exist("t_commit");
        System.out.println(rlt);
        System.out.println(tableDao.OwnerDB());

        System.out.println("============");

        for (var entry : tableDao.ShowColumn("t_commit")) {
            System.out.println(entry);
        }

        System.out.println("============");

        for (var one : tableDao.ShowComment("t_commit")) {
            System.out.println(one);
        }

        System.out.println("============");

        for (var entry : tableDao.ShowIndex("t_commit")) {
            System.out.println(entry);
        }
    }

    @Test
    void CreateTable() {
        // tableDao.Drop("t_commit_hgglb");
        commit_createDao.Create("t_commit_hgglb");
    }

    @Test
    void distinct() {
        var ls = tableDao.FieldList("t_commit_30015", "userId", false);
    }
}
