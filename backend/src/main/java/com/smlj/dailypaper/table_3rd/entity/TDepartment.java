package com.smlj.dailypaper.table_3rd.entity;

import java.util.Boolean;
import java.util.Date;
import java.io.Serializable;
import java.io.Serial;

import lombok.Data;

/**
 * (TDepartment)表实体类
 *
 * @author Cui
 * @since 2024-08-18 17:37:58
 */
@Data
public class TDepartment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1; // -57422058565690574L;

    private String id;
    private String create_by;
    private Date create_time;
    private Integer del_flag;
    private String update_by;
    private Date update_time;
    private String parent_id;
    private Double sort_order;
    private Integer status;
    private String title;
    private Boolean is_parent;
    private Integer open_status;
    private String code;
    private String parent_code;
}

