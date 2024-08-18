package com.smlj.dailypaper.table_3rd.service.impl;

import com.smlj.dailypaper.table_3rd.entity.TUser;
import com.smlj.dailypaper.table_3rd.dao.TUserDao;
import com.smlj.dailypaper.table_3rd.service.TUserService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import com.smlj.dailypaper.config.db.EDatasource;

/**
 * (TUser)表服务实现类
 *
 * @author Cui
 * @since 2024-08-18 17:57:32
 */
@Slf4j
@DS(EDatasource.jtmenhu)
@Service("TUserService")
public class TUserServiceImpl implements TUserService {
    @Autowired
    private TUserDao tUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TUser queryById(String id) {
        return this.tUserDao.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    @Override
    public TUser insert(TUser tUser) {
        this.tUserDao.insert(tUser);
        return tUser;
    }

    /**
     * 修改数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    @Override
    public TUser update(TUser tUser) {
        this.tUserDao.update(tUser);
        return this.queryById(tUser.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.tUserDao.deleteById(id) > 0;
    }
}
