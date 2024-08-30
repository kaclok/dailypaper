package com.smlj.dailypaper.controller;

import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.service.TCommitService;
import com.smlj.dailypaper.table.service.TUserService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@EnableScheduling
@RequestMapping("/table")
public class Table {
    static public void TryFillUser(String userAccount, String userTableName, com.smlj.dailypaper.table_3rd.service.TUserService jt_userService, TUserService userService, TableDao tableDao) {
        if (tableDao.Exist(userTableName) <= 0) {
            // 构建部门的user表
            userService.Create(userTableName);

            // 填充部门的user表
            ArrayList<com.smlj.dailypaper.table_3rd.entity.TUser> rlts = jt_userService.selectByAccount(userAccount);
            ArrayList<com.smlj.dailypaper.table.entity.TUser> list = new ArrayList<>();
            for (int i = 0; i < rlts.size(); i++) {
                var one = rlts.get(i);
                TUser user = new TUser();
                user.setId(i + 1);
                user.setName(one.getNickname());
                user.setAccount(one.getUsername());

                list.add(user);
            }

            userService.InsertBatch(userTableName, list);
        }
    }

    // 10天执行一次
    // 定期删除commit中无用的提交条目
    @Scheduled(fixedDelay = 1000 * 3600 * 24 * 10)
    private void deleteUnusedOnCommitTable() {
        log.info("-- 定时任务 --");
    }

    static public void TryCreateCommit(String commitTableName, TCommitService commitService, TableDao tableDao) {
        if (tableDao.Exist(commitTableName) <= 0) {
            commitService.Create(commitTableName);
        }
    }

    static public String getUserTableName(int departmentCode) {
        return "t_user_" + departmentCode;
    }

    static public String getCommitTableName(int departmentCode) {
        return "t_commit_" + departmentCode;
    }

    static public String getDateCommitTableName(int departmentCode) {
        return "t_datecommit_" + departmentCode;
    }
}
