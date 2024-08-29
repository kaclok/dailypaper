package com.smlj.dailypaper.controller;

import com.smlj.dailypaper.proto.to.common.Result;
import com.smlj.dailypaper.table.dao.TCommitDao;
import com.smlj.dailypaper.table.dao.TUserDao;
import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RestController
@RequestMapping("/table")
public class Table {
    private final Lock lockUser = new ReentrantLock();
    private final Lock lockCommit = new ReentrantLock();

    @Autowired
    private TUserDao userDao;

    @Autowired
    private TCommitDao commitDao;

    @Autowired
    private TableDao tableDao;

    @GetMapping("/create_table_user")
    private Result<Boolean> create_table_user(@RequestParam("departmentCode") int departmentCode, @RequestParam(name = "hash", required = false) Integer hash) {
        var r = new ResultUtil<Boolean>();
        if (hash == null || hash != (7 + departmentCode)) {
            return r.setErrorMsg("hash not valid!", null);
        }

        return _create_table_user(departmentCode);
    }

    private Result<Boolean> _create_table_user(int departmentCode) {
        lockUser.lock();
        try {
            var r = new ResultUtil<Boolean>();
            String tableName = "t_user_" + departmentCode;
            String db = tableDao.OwnerDB();
            log.info("db:" + db);
            int exist = tableDao.Exist(tableName);
            if(exist >= 1) {
                return r.setErrorMsg("has exist table", null);
            }

            userDao.Create(tableName);
            return r.setSuccessMsg("create_table_user success", null);
        } finally {
            lockUser.unlock();
        }
    }

    @GetMapping("/create_table_commit")
    private Result<Boolean> create_table_commit(@RequestParam("departmentCode") int departmentCode, @RequestParam(name = "hash", required = false) Integer hash) {
        var r = new ResultUtil<Boolean>();
        if (hash == null || hash != (7 + departmentCode)) {
            return r.setErrorMsg("hash not valid!", null);
        }

        return _create_table_commit(departmentCode);
    }

    private Result<Boolean> _create_table_commit(int departmentCode) {
        lockCommit.lock();
        try {
            var r = new ResultUtil<Boolean>();
            String tableName = "t_commit_" + departmentCode;
            int exist = tableDao.Exist(tableName);
            if(exist >= 1) {
                return r.setErrorMsg("has exist table", null);
            }

            commitDao.Create(tableName);
            return r.setSuccessMsg("create_table_commit success", null);
        } finally {
            lockCommit.unlock();
        }
    }
}
