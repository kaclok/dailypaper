package com.smlj.dailypaper.controller;

import com.smlj.dailypaper.proto.to.common.Result;
import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.service.TCommitService;
import com.smlj.dailypaper.table.service.TUserService;
import com.smlj.dailypaper.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RestController
@RequestMapping("/table")
public class Table {
    private final Lock lockUser = new ReentrantLock();
    private final Lock lockCommit = new ReentrantLock();

    @Autowired
    private TUserService userService;

    @Autowired
    private TCommitService commitService;

    @Autowired
    private com.smlj.dailypaper.table_3rd.service.TUserService jt_userService;

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

    public Result<Boolean> _create_table_user(int departmentCode) {
        lockUser.lock();
        try {
            var r = new ResultUtil<Boolean>();
            String tableName = "t_user_" + departmentCode;
            String db = tableDao.OwnerDB();
            int exist = tableDao.Exist(tableName);
            if(exist >= 1) {
                return r.setErrorMsg("has exist table", null);
            }

            userService.Create(tableName);
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

    public Result<Boolean> _create_table_commit(int departmentCode) {
        lockCommit.lock();
        try {
            var r = new ResultUtil<Boolean>();
            String tableName = "t_commit_" + departmentCode;
            int exist = tableDao.Exist(tableName);
            if(exist >= 1) {
                return r.setErrorMsg("has exist table", null);
            }

            commitService.Create(tableName);
            return r.setSuccessMsg("create_table_commit success", null);
        } finally {
            lockCommit.unlock();
        }
    }
}
