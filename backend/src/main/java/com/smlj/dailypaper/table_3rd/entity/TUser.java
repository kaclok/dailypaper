package com.smlj.dailypaper.table_3rd.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;

import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * (TUser)表实体类
 *
 * @author Cui
 * @since 2024-08-19 08:29:19
 */
@Data
@Component("jtmenhu_TUser")
public class TUser implements Serializable {
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

