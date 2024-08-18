package com.smlj.dailypaper.table_3rd.service;

import com.smlj.dailypaper.table_3rd.entity.TDepartment;

/**
 * (TDepartment)表服务接口
 *
 * @author Cui
 * @since 2024-08-18 17:37:58
 */
public interface TDepartmentService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TDepartment queryById(String id);

    /**
     * 新增数据
     *
     * @param tDepartment 实例对象
     * @return 实例对象
     */
    TDepartment insert(TDepartment tDepartment);

    /**
     * 修改数据
     *
     * @param tDepartment 实例对象
     * @return 实例对象
     */
    TDepartment update(TDepartment tDepartment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);
}
