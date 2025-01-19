package com.smlj.dailypaper;

import com.smlj.dailypaper.table.dao.TCommitDao;
import com.smlj.dailypaper.table.dao.TDateCommitDao;
import com.smlj.dailypaper.table.dao.TUserDao;
import com.smlj.dailypaper.table.dao.common.TableDao;
import com.smlj.dailypaper.table.entity.TUser;

import com.smlj.dailypaper.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest(classes = {DailypaperApplication.class})
class DailypaperApplicationTests {
    @Test
    void ExistTable() {
       int weekNight = (int) DateTimeUtil.convertToWeekMidnightTimestamp(1737216000);
        log.error(weekNight + "");
    }
}
