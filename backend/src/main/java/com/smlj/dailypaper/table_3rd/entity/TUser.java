package com.smlj.dailypaper.table_3rd.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;
import java.io.Serial;

import lombok.Data;

/**
 * (TUser)表实体类
 *
 * @author Cui
 * @since 2024-08-18 17:37:59
 */
@Data
public class TUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1; // 256355531036328527L;

    private String id;
    private String create_by;
    private Date create_time;
    private String update_by;
    private Date update_time;
    private String address;
    private String avatar;
    private String description;
    private String email;
    private String mobile;
    private String nickname;
    private String password;
    private String sex;
    private Integer status;
    private Integer type;
    private String username;
    private Integer del_flag;
    private String department_id;
    private String street;
    private String pass_strength;
    private String department_title;
    private LocalDateTime birth;
    private Integer open_status;
}
