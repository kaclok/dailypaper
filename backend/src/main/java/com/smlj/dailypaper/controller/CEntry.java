package com.smlj.dailypaper.controller;

import com.smlj.dailypaper.proto.to.To_DateCommit;
import com.smlj.dailypaper.proto.to.To_Excel;
import com.smlj.dailypaper.proto.to.To_ExcelRow;
import com.smlj.dailypaper.proto.to.To_UserCommit;
import com.smlj.dailypaper.proto.to.common.Result;
import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.table.entity.TCommit;
import com.smlj.dailypaper.table.entity.TUser;
import com.smlj.dailypaper.table.service.TCommitService;
import com.smlj.dailypaper.table.service.TDateCommitService;
import com.smlj.dailypaper.table.service.TUserService;
import com.smlj.dailypaper.utils.DateTimeUtil;
import com.smlj.dailypaper.utils.ResultUtil;
import com.smlj.dailypaper.utils.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class CEntry {
    private final com.smlj.dailypaper.table.service.TUserService userService;

    private final com.smlj.dailypaper.table_3rd.service.TUserService jt_userService;

    private final TDateCommitService dateCommitService;

    private final TCommitService commitService;

    private final TableDao tableDao;

    public CEntry(TUserService userService, com.smlj.dailypaper.table_3rd.service.TUserService jtUserService, TDateCommitService dateCommitService, TCommitService commitService, TableDao tableDao) {
        this.userService = userService;
        jt_userService = jtUserService;
        this.dateCommitService = dateCommitService;
        this.commitService = commitService;
        this.tableDao = tableDao;
    }

    // GetMapping如何截取url参数(考虑参数的可选还是必选)： https://blog.csdn.net/m0_51390969/article/details/135880395
    @GetMapping("/getAll")
    public Result<To_DateCommit> GetAll(@RequestParam("userCard") String userCard, @RequestParam("date") long date, HttpServletRequest request) {
        log.info("GetAll:{}", UrlUtil.GetFullUrl(request));

        try {
            var r = new ResultUtil<To_DateCommit>();
            if (userCard == null || userCard.isEmpty() || date <= 0) {
                return r.setErrorMsg("args invalid!", null);
            }

            String departmentId = jt_userService.selectMembersByAccount(userCard).getFirst().getDeptCode();
            String userTableName = Table.getUserTableName(departmentId);
            try {
                Table.TryFillUser(userCard, userTableName, jt_userService, userService, tableDao, departmentId);
            } catch (Exception e) {
                log.info(e.getMessage());
            }

            // 构建部门的commit表
            String commitTableName = Table.getCommitTableName(departmentId);
            Table.TryCreateCommit(commitTableName, commitService, tableDao);

            int midNight = (int) DateTimeUtil.convertToMidnightTimestamp(date);
            int count = userService.Count(userTableName);

            To_DateCommit to = new To_DateCommit();
            to.setTotal(count);
            to.setDate(midNight);
            to.setDepartmentId(departmentId);

            var isLeader = userService.GetUserById(userTableName, userCard).isLeader();
            to.setCurUserIsLeader(isLeader);

            String departmentName = jt_userService.getDepartmentName(departmentId);
            to.setDepartmentName(departmentName);

            List<TUser> users = userService.FindAll(userTableName);
            // 构建部门的datecommit表
            String dateCommitTableName = Table.getDateCommitTableName(departmentId);
            Table.TryCreateDateCommit(dateCommitTableName, dateCommitService, tableDao, users);

            HashMap<String, Object> dateCommit = dateCommitService.FindBy(dateCommitTableName, midNight);
            if (dateCommit == null) {
                dateCommitService.InsertEmpty(dateCommitTableName, midNight);
                // 重新db中查找
                dateCommit = dateCommitService.FindBy(dateCommitTableName, midNight);
            }

            for (int i = 0; i < users.size(); i++) {
                var user = users.get(i);
                if (!user.isEnable()) {
                    continue;
                }

                To_UserCommit tu = new To_UserCommit();
                tu.setUserId(user.getId());
                tu.setName(user.getName());
                tu.setAccount(user.getAccount());
                tu.setLeader(user.isLeader());

                String key = "userId_" + user.getId();
                Long commitId = (Long) (dateCommit.get(key));
                TCommit c = commitService.FindById(commitTableName, commitId.intValue());
                if (c != null) {
                    tu.setTime(c.getCommitDateTime());
                    tu.setContent(c.getContent());
                    tu.setTomorrowPlan(c.getTomorrowPlan());
                    tu.setTomorrowArrangement(c.getTomorrowArrangement());
                }

                to.getCommits().add(tu);
            }

            // log.info("getAll-> to:{}", to);
            return r.setData(to, "getAll");
        } finally {
        }
    }

    @GetMapping("/edit")
    @Transactional
    public Result<To_DateCommit> Edit(@RequestParam("departmentId") String departmentId, @RequestParam("date") long date, @RequestParam("userId") String userId, @RequestParam("content") String content, @RequestParam(value = "tomorrowPlan", required = false) String tomorrowPlan,
                                      @RequestParam(value = "tomorrowArrangement", required = false) String tomorrowArrangement, @RequestParam(name = "hash", required = false) Integer hash, HttpServletRequest request) {
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
                String datecommitTableName = Table.getDateCommitTableName(departmentId);
                var targetMidNight = DateTimeUtil.convertToMidnightTimestamp(date);
                var dateCommit = dateCommitService.FindBy(datecommitTableName, targetMidNight);
                if (dateCommit == null) {
                    // 如果date对应的记录不存在的话，立即插入新纪录
                    dateCommitService.InsertEmpty("t_DateCommit", targetMidNight);
                }

                TCommit cm = new TCommit();
                cm.setUserId(userId);
                cm.setCommitDateTime(DateTimeUtil.nowTimestamp());
                cm.setContent(content);
                cm.setTomorrowPlan(tomorrowPlan);
                cm.setTomorrowArrangement(tomorrowArrangement);

                String commitTableName = Table.getCommitTableName(departmentId);
                // 插入commit表
                commitService.InsertOutKey(commitTableName, cm);
                // todo 并发的时候是否会出现问题？
                int lastId = cm.getId();
                log.info("edit insert id: {}", lastId);
                // 更新datecommit表
                dateCommitService.Update(datecommitTableName, targetMidNight, "userId_" + userId, lastId);

                return r.setSuccessMsg("edit success", null);
            }
        } finally {
        }
    }

    @GetMapping("/export_all")
    public Result<To_Excel<To_ExcelRow>> ExportAll(@RequestParam("departmentId") String departmentId, @RequestParam("beginDate") long beginDate, @RequestParam("endDate") long endDate, HttpServletRequest request) {
        try {
            log.info("ExportAll: {}", UrlUtil.GetFullUrl(request));

            String datecommitTableName = Table.getDateCommitTableName(departmentId);
            String commitTableName = Table.getCommitTableName(departmentId);
            String userTableName = Table.getUserTableName(departmentId);

            List<TUser> users = userService.FindAll(userTableName);
            To_Excel<To_ExcelRow> rlt = new To_Excel<>();
            // 获取excel第一行
            rlt.getColNames().add("日期");
            if (users != null) {
                for (var user : users) {
                    String name = user.getName();
                    rlt.getColNames().add(name + ":今日");
                    rlt.getColNames().add(name + ":明日计划");
                    rlt.getColNames().add(name + ":明日安排");
                }
            }

            ArrayList<HashMap<String, Object>> cs = dateCommitService.GetRangeCommits(datecommitTableName, beginDate, endDate);
            for (HashMap<String, Object> one : cs) {
                To_ExcelRow excelRow = new To_ExcelRow();
                Long date = (Long) one.get("date");
                excelRow.setTime(date.intValue());
                boolean allEmpty = true;

                for (var user : users) {
                    String key = "userId_" + user.getId();
                    Long commitId = (Long) (one.get(key));
                    String content = null;
                    String tomorrowPlan = null;
                    if (commitId != null && commitId != 0) {
                        var c = commitService.FindById(commitTableName, commitId.intValue());
                        if (c != null) {
                            content = c.getContent();
                            content = content == null ? "" : content;
                            tomorrowPlan = c.getTomorrowPlan();
                            tomorrowPlan = tomorrowPlan == null ? "" : tomorrowPlan;
                        }
                    }

                    allEmpty &= (content == null || content.isEmpty());
                    excelRow.getContents().add(content);
                    excelRow.getContents().add(tomorrowPlan);
                }

                if (!allEmpty) {
                    rlt.getRows().add(excelRow);
                }
            }

            var r = new ResultUtil<To_Excel<To_ExcelRow>>();
            return r.setSuccessMsg("edit success", rlt);
        } finally {
        }
    }
}
