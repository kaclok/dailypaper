package com.smlj.dailypaper.controller;

import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.table.entity.TDateCommit;
import com.smlj.dailypaper.table.entity.TCommit;
import com.smlj.dailypaper.proto.to.To_DateCommit;
import com.smlj.dailypaper.proto.to.To_ExcelRow;
import com.smlj.dailypaper.proto.to.To_Excel;
import com.smlj.dailypaper.proto.to.To_UserCommit;
import com.smlj.dailypaper.proto.to.common.Result;
import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.service.TCommitService;
import com.smlj.dailypaper.table.service.TDateCommitService;
import com.smlj.dailypaper.table.service.TUserService;
import com.smlj.dailypaper.utils.UrlUtil;
import com.smlj.dailypaper.utils.DateTimeUtil;
import com.smlj.dailypaper.utils.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.ArrayList;

// https://www.bilibili.com/video/BV1Lq4y1J77x?p=16&spm_id_from=pageDriver&vd_source=5c9f5bd891aee351c325bcf632b5550f 整合redis

// https://blog.csdn.net/miles067/article/details/132567377
// @RestController 是一个组合注解，它结合了 @Controller 和 @ResponseBody 注解的功能（就相当于把两个注解组合在一起）。在使用 @RestController 注解标记的类中，每个方法的返回值都会以 JSON 或 XML 的形式直接写入 HTTP 响应体中，相当于在每个方法上都添加了 @ResponseBody 注解。

// @RequestMapping是Spring MVC中用于映射web请求（如URL路径）到具体的方法上的注解。它既可以标注在类上，也可以标注在方法上。标注在类上时，表示类中的所有响应请求的方法都是以该类路径为父路径
// @GetMapping用于将HTTP get请求映射到特定处理程序的方法注解,具体来说，@GetMapping是一个组合注解，是@RequestMapping(method = RequestMethod.GET)的缩写
// @PostMapping用于将HTTP post请求映射到特定处理程序的方法注解,具体来说，@PostMapping是一个组合注解，是@RequestMapping(method = RequestMethod.POST)的缩写

// SpringBoot读取配置文件方式：https://www.bilibili.com/video/BV1Lq4y1J77x?p=10&spm_id_from=pageDriver&vd_source=5c9f5bd891aee351c325bcf632b5550f
// 1、字段@Value
// 2、Environment， autowired标记
// 3、@ConfigurationProperties， 自定义bean类类头标记， @component表示是bean类，@ConfigurationProperties一般需要指定前缀。（类名和配置的前缀绑定了，然后controller中autowired自定义bean类实例）
@Slf4j
@RestController
@RequestMapping("/dailypaper")
public class Entry {
    @Autowired
    private TUserService userService;

    @Autowired
    private com.smlj.dailypaper.table_3rd.service.TUserService jt_userService;

    @Autowired
    private TDateCommitService dateCommitService;

    @Autowired
    private TCommitService commitService;

    @Autowired
    private TableDao tableDao;

    @Autowired
    private HttpServletRequest request;

    private final Lock lockGetall = new ReentrantLock();
    private final Lock lockEdit = new ReentrantLock();
    private final Lock lockExportAll = new ReentrantLock();
    private final Lock lockExportOne = new ReentrantLock();

    // GetMapping如何截取url参数(考虑参数的可选还是必选)： https://blog.csdn.net/m0_51390969/article/details/135880395
    @GetMapping("/getAll")
    private Result<To_DateCommit> GetAll(@RequestParam("userAccount") String userAccount, @RequestParam("date") long date) {
        log.info("GetAll:{}", UrlUtil.GetFullUrl(request));

        var r = new ResultUtil<To_DateCommit>();
        if (userAccount == null || userAccount.isEmpty() || date <= 0) {
            return r.setErrorMsg("args invalid!", null);
        }

        // todo 将来redis构建userAccount和departName的关系

        Integer departmentCode = jt_userService.getDepartmentCode(userAccount);
        // 默认：数字化中心
        departmentCode = departmentCode == null ? 30015 : departmentCode;

        String userTableName = Table.getUserTableName(departmentCode);
        if (tableDao.Exist(userTableName) <= 0) {
            // 构建部门的user表 ------------------------------------------------------------------------------------------
            userService.Create(userTableName);

            // 填充部门的user表 ------------------------------------------------------------------------------------------
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

        // 构建部门的commit表 --------------------------------------------------------------------------------------------
        String commitTableName = Table.getCommitTableName(departmentCode);
        if (tableDao.Exist(commitTableName) <= 0) {
            commitService.Create(commitTableName);
        }

        var midNight = DateTimeUtil.convertToMidnightTimestamp(date);
        var dateCommit = dateCommitService.FindBy("t_DateCommit", midNight);

        To_DateCommit to = new To_DateCommit();
        to.setTotal(TDateCommit.GetFieldCount());
        to.setDate(midNight);
        to.setDepartmentId(departmentCode);
        String departmentName = jt_userService.getDepartmentName(userAccount);
        departmentName = departmentName == null ? "未知部门" : departmentName;
        to.setDepartmentName(departmentName);

        String dateCommitTableName = "t_datecommit_" + departmentCode;
        if (dateCommit == null) {
            lockGetall.lock();
            try {
                dateCommitService.Insert("t_DateCommit", midNight);
                dateCommit = new TDateCommit(midNight);
            } finally {
                lockGetall.unlock();
            }
        }

        ArrayList<Integer> commitIds = dateCommit.GetAllIds();
        var userIds = dateCommit.GetFieldIds();
        for (int i = 0; i < commitIds.size(); i++) {
            var userId = userIds.get(i);
            To_UserCommit tu = new To_UserCommit();
            var user = userService.GetUserById(userTableName, userId);
            tu.setUserId(userId);
            tu.setName(user.getName());
            tu.setAccount(user.getAccount());

            var commitId = commitIds.get(i);
            TCommit c = commitService.FindById(commitTableName, commitId);
            if (c != null) {
                tu.setTime(c.getCommitDateTime());
                tu.setContent(c.getContent());
            }

            to.getCommits().add(tu);
        }

        // log.info("getAll-> to:{}", to);
        return r.setData(to, "getAll");
    }

    @GetMapping("/edit")
    private Result<To_DateCommit> Edit(@RequestParam("departmentId") int departmentId, @RequestParam("date") long date, @RequestParam("userId") int userId, @RequestParam("content") String content,
                                       @RequestParam(name = "hash", required = false) Integer hash) {
        lockEdit.lock();
        try {
            var now = System.currentTimeMillis() / 1000;
            var todayMidNight = DateTimeUtil.convertToMidnightTimestamp(now);

            log.info("Edit: {} -> now:{}, todayMidNight:{}", UrlUtil.GetFullUrl(request), now, todayMidNight);

            var r = new ResultUtil<To_DateCommit>();
            if (hash == null || hash != (7 + content.length())) {
                // 往日的日报信息不能编辑
                return r.setErrorMsg("hash not valid!", null);
            } else if (date + 86400 * 1 < todayMidNight) {
                // 往日的日报信息不能编辑
                return r.setErrorMsg("Can not edit because not today!", null);
            } else {
                var targetMidNight = DateTimeUtil.convertToMidnightTimestamp(date);
                var dateCommit = dateCommitService.FindBy("t_DateCommit", targetMidNight);
                if (dateCommit == null) {
                    // 如果date对应的记录不存在的话，立即插入新纪录
                    dateCommitService.Insert("t_DateCommit", targetMidNight);
                }

                TCommit cm = new TCommit();
                cm.setUserId(userId);
                cm.setCommitDateTime(DateTimeUtil.nowTimestamp());
                cm.setContent(content);

                String commitTableName = Table.getCommitTableName(departmentId);
                // 插入commit表
                commitService.InsertOutKey(commitTableName, cm);
                // todo 并发的时候是否会出现问题？
                int lastId = cm.getId();
                log.info("edit insert id: {}", lastId);
                // 更新datecommit表
                dateCommitService.Update("t_DateCommit", targetMidNight, "userId_" + userId, lastId);

                return r.setSuccessMsg("edit success", null);
            }
        } finally {
            lockEdit.unlock();
        }
    }

    @GetMapping("/export_one")
    private Result<To_Excel<To_ExcelRow>> ExportOne(@RequestParam("departmentId") int departmentId, @RequestParam("userId") short userId, @RequestParam("beginDate") long beginDate, @RequestParam("endDate") long endDate) {
        lockExportOne.lock();
        try {
            log.info("ExportOne: {}", UrlUtil.GetFullUrl(request));

            To_Excel<To_ExcelRow> rlt = new To_Excel<>();
            ArrayList<TDateCommit> cs = dateCommitService.GetRangeCommitsByUser("t_DateCommit", beginDate, endDate, "userId_" + userId);

            String userTableName = Table.getUserTableName(departmentId);
            rlt.getColNames().add("日期");
            var user = userService.GetUserById(userTableName, userId);
            String colName = Integer.toString(userId);
            if (user != null) {
                colName = user.getName();
            }
            rlt.getColNames().add(colName);

            String commitTableName = Table.getCommitTableName(departmentId);
            for (TDateCommit row : cs) {
                To_ExcelRow excelRow = new To_ExcelRow();
                var commitId = row.GetBy(userId);
                String content = null;
                if (commitId != 0) {
                    var c = commitService.FindById(commitTableName, commitId);
                    if (c != null) {
                        content = c.getContent();
                    }
                }

                if (content != null) {
                    excelRow.getContents().add(content);
                    excelRow.setTime(row.getDate());

                    rlt.getRows().add(excelRow);
                }
            }

            var r = new ResultUtil<To_Excel<To_ExcelRow>>();
            return r.setSuccessMsg("edit success", rlt);
        } finally {
            lockExportOne.unlock();
        }
    }

    @GetMapping("/export_all")
    private Result<To_Excel<To_ExcelRow>> ExportAll(@RequestParam("departmentId") int departmentId, @RequestParam("beginDate") long beginDate, @RequestParam("endDate") long endDate) {
        lockExportAll.lock();
        try {
            log.info("ExportAll: {}", UrlUtil.GetFullUrl(request));

            To_Excel<To_ExcelRow> rlt = new To_Excel<>();
            ArrayList<TDateCommit> cs = dateCommitService.GetRangeCommits("t_DateCommit", beginDate, endDate);
            boolean hasGetColNames = false;
            String commitTableName = Table.getCommitTableName(departmentId);
            String userTableName = Table.getUserTableName(departmentId);
            for (TDateCommit one : cs) {
                To_ExcelRow excelRow = new To_ExcelRow();
                excelRow.setTime(one.getDate());
                boolean allEmpty = true;

                if (!hasGetColNames) {
                    rlt.getColNames().add("日期");
                    var userIds = one.GetFieldIds();
                    for (var userId : userIds) {
                        var user = userService.GetUserById(userTableName, userId);
                        String colName = Integer.toString(userId);
                        if (user != null) {
                            colName = user.getName();
                        }

                        rlt.getColNames().add(colName);
                    }

                    hasGetColNames = true;
                }

                for (var commitId : one.GetAllIds()) {
                    String content = null;
                    if (commitId != 0) {
                        var c = commitService.FindById(commitTableName, commitId);
                        if (c != null) {
                            content = c.getContent();
                        }
                    }

                    allEmpty &= (content == null);
                    excelRow.getContents().add(content);
                }

                if (!allEmpty) {
                    rlt.getRows().add(excelRow);
                }
            }

            var r = new ResultUtil<To_Excel<To_ExcelRow>>();
            return r.setSuccessMsg("edit success", rlt);
        } finally {
            lockExportAll.unlock();
        }
    }
}
