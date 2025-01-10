package com.smlj.dailypaper.controller;

import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@EnableScheduling
@RequestMapping("/table")
public class Table {
    private final com.smlj.dailypaper.table_3rd.service.TUserService jt_userService;
    private final TableDao tableDao;

    public Table(com.smlj.dailypaper.table_3rd.service.TUserService jtUserService, TableDao tableDao, StringRedisTemplate redis) {
        jt_userService = jtUserService;
        this.tableDao = tableDao;
    }

    static public void TryFillUser(String userCard, String tableName, com.smlj.dailypaper.table_3rd.service.TUserService jt_userService, TUserService userService, TableDao tableDao, String departmentCode) {
        if (tableDao.Exist(tableName) <= 0) {
            // 构建部门的user表
            userService.Create(tableName);

            com.smlj.dailypaper.table_3rd.entity.TUser leader = jt_userService.getLeader(departmentCode);
            // 填充部门的user表
            ArrayList<com.smlj.dailypaper.table_3rd.entity.TUser> rlts = jt_userService.selectMembersByAccount(userCard);
            ArrayList<com.smlj.dailypaper.table.entity.TUser> list = new ArrayList<>();
            for (int i = 0; i < rlts.size(); i++) {
                var one = rlts.get(i);
                TUser user = new TUser();
                user.setId(one.getAccount());
                user.setName(one.getName());
                user.setAccount(one.getCard());
                user.setEnable(true);
                if (leader != null) {
                    user.setLeader(leader.getAccount().equals(one.getAccount()));
                } else {
                    user.setLeader(false);
                }

                list.add(user);
            }

            userService.InsertBatch(tableName, list);
        }
    }

    // 10天执行一次
    // 定期删除commit中无用的提交条目
    @Scheduled(fixedDelay = 1000 * 3600 * 24 * 10)
    private void deleteUnusedOnCommitTable() {
        log.info("-- 定时任务 --");
    }

    static public void TryCreateCommit(String tableName, TCommitService commitService, TableDao tableDao) {
        if (tableDao.Exist(tableName) <= 0) {
            commitService.Create(tableName);
        }
    }

    static public void TryCreateDateCommit(String tableName, TDateCommitService datecommitService, TableDao tableDao, List<TUser> users) {
        if (tableDao.Exist(tableName) <= 0) {
            ArrayList<String> list = new ArrayList<>(users.size());
            for (TUser user : users) {
                list.add(user.getId());
            }
            datecommitService.Create(tableName, list);
        }
    }

    static public void TryCreateWeekPlanCommit(String tableName, TWeekPlanCommitService commitService, TableDao tableDao) {
        if (tableDao.Exist(tableName) <= 0) {
            commitService.Create(tableName);
        }
    }

    static public void TryCreateWeekPlanDateCommit(String tableName, TWeekPlanDateCommitService datecommitService, TableDao tableDao, List<TUser> users) {
        if (tableDao.Exist(tableName) <= 0) {
            ArrayList<String> list = new ArrayList<>(users.size());
            for (TUser user : users) {
                list.add(user.getId());
            }
            datecommitService.Create(tableName, list);
        }
    }

    static public String getUserTableName(String departmentCode) {
        return "t_user_" + departmentCode;
    }

    static public String getCommitTableName(String departmentCode) {
        return "t_commit_" + departmentCode;
    }

    static public String getDateCommitTableName(String departmentCode) {
        return "t_datecommit_" + departmentCode;
    }

    static public String getWeekPlanDateCommitTableName(String departmentCode) {
        return "t_weekplan_datecommit_" + departmentCode;
    }

    static public String getWeekPlanCommitTableName(String departmentCode) {
        return "t_weekplan_commit_" + departmentCode;
    }
}
