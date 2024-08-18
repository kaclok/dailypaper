package com.smlj.dailypaper.table_3rd.dao;

import com.smlj.dailypaper.table_3rd.entity.TDepartment;

import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TDepartment)表数据库访问层
 *
 * @author Cui
 * @since 2024-08-18 17:37:58
 */
@Repository
@Mapper
public interface TDepartmentDao {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TDepartment queryById(String id);

    /**
     * 统计总行数
     *
     * @param tDepartment 查询条件
     * @return 总行数
     */
    long count(TDepartment tDepartment);

    /**
     * 新增数据
     *
     * @param tDepartment 实例对象
     * @return 影响行数
     */
    int insert(TDepartment tDepartment);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TDepartment> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TDepartment> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TDepartment> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TDepartment> entities);

    /**
     * 修改数据
     *
     * @param tDepartment 实例对象
     * @return 影响行数
     */
    int update(TDepartment tDepartment);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);
}

