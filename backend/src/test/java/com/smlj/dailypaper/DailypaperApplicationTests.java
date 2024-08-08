package com.smlj.dailypaper;

import com.smlj.dailypaper.entity.po.tUser;
import com.smlj.dailypaper.mapper.CommitMapper;
import com.smlj.dailypaper.mapper.DateCommitMapper;
import com.smlj.dailypaper.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DailypaperApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DateCommitMapper dateCommitMapper;

    @Autowired
    private CommitMapper commitMapper;

    @Test
    void testFindAll() {
        List<tUser> list = userMapper.FindAll();
        System.out.println(list);
    }
}
