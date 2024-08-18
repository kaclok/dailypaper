package com.smlj.dailypaper.table_3rd.service;

import com.smlj.dailypaper.table_3rd.entity.TUser;

/**
 * (TUser)表服务接口
 *
 * @author Cui
 * @since 2024-08-18 17:37:59
 */
public interface TUserService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TUser queryById(String id);

    /**
     * 新增数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    TUser insert(TUser tUser);

    /**
     * 修改数据
     *
     * @param tUser 实例对象
     * @return 实例对象
     */
    TUser update(TUser tUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);
}
