package com.smlj.dailypaper.table_3rd.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;
import java.io.Serial;

import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * (TUser)表实体类
 *
 * @author Cui
 * @since 2024-08-19 08:29:19
 */
@Data
@Component("train_TUser")
public class TUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 1; // -87089175252424814L;

    private String account;

    private String name;

    private String pwd;

    private String deptCode;

    private String orgCode;

    private String mobile;

    private String card;

    private Integer role;

    private boolean openStatus;

    private boolean isChangedPwd;
}

