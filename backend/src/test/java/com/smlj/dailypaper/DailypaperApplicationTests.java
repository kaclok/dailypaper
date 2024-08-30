package com.smlj.dailypaper;

import com.smlj.dailypaper.table.dao.TCommitDao;
import com.smlj.dailypaper.table.dao.TDateCommitDao;
import com.smlj.dailypaper.table.dao.TUserDao;
import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.table.entity.TDateCommit;
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

        for (String one : tableDao.ShowComment("t_commit")) {
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
    void FillUser() {
        int departmentCode = 30015;
        String userAccount = "SMLJ23659";
        String tableName = "t_user_" + departmentCode;
        ArrayList<com.smlj.dailypaper.table_3rd.entity.TUser> rlt = jt_userService.selectByAccount(userAccount);

        ArrayList<com.smlj.dailypaper.table.entity.TUser> list = new ArrayList<>();
        for (int i = 0; i < rlt.size(); i++) {
            var one = rlt.get(i);
            com.smlj.dailypaper.table.entity.TUser user = new com.smlj.dailypaper.table.entity.TUser();
            user.setId(i + 1);
            user.setName(one.getNickname());
            user.setAccount(one.getUsername());

            list.add(user);
        }

        tUserDao.InsertBatch(tableName, list);
    }

    @Test
    void CreateDateCommitTable() {
        int departmentCode = 30015;
        String tableName = "t_datecommit_" + departmentCode;

        tableDao.Drop(tableName);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        list.add("16");
        list.add("17");
        datecommit_createDao.Create(tableName, list);
    }
}
