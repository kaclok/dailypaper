package com.smlj.dailypaper.table_3rd.service.impl;

import com.smlj.dailypaper.table_3rd.entity.TDepartment;
import com.smlj.dailypaper.table_3rd.dao.TDepartmentDao;
import com.smlj.dailypaper.table_3rd.service.TDepartmentService;
import com.smlj.dailypaper.config.db.EDatasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * (TDepartment)表服务实现类
 *
 * @author Cui
 * @since 2024-08-19 08:43:23
 */
@Slf4j
@DS(EDatasource.jtmenhu)
@Service("jtmenhu_TDepartmentService")
public class TDepartmentServiceImpl implements TDepartmentService {
    @Autowired
    @Qualifier("jtmenhu_TDepartmentDao")
    private TDepartmentDao tDepartmentDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TDepartment queryById(String id) {
        return this.tDepartmentDao.queryById(id);
    }

    /**
     * 新增数据
     *
     * @param tDepartment 实例对象
     * @return 实例对象
     */
    @Override
    public TDepartment insert(TDepartment tDepartment) {
        this.tDepartmentDao.insert(tDepartment);
        return tDepartment;
    }

    /**
     * 修改数据
     *
     * @param tDepartment 实例对象
     * @return 实例对象
     */
    @Override
    public TDepartment update(TDepartment tDepartment) {
        this.tDepartmentDao.update(tDepartment);
        return this.queryById(tDepartment.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.tDepartmentDao.deleteById(id) > 0;
    }
}
