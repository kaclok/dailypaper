package com.smlj.dailypaper.entity.vo.to.common;

import com.smlj.dailypaper.utils.DateTimeUtil;
import lombok.Data;

import java.io.Serializable;

// 前后端交互数据标准
@Data
public class Result<T> implements Serializable {
    // 成功、失败标志
    private boolean result;
    // 成功、失败原因
    private String reason;

    // 返回代码
    private int code;
    // 时间戳
    private long timestamp = DateTimeUtil.nowTimestamp();

    // 结果对象
    private T data;

    public void empty() {
        this.result = false;
        this.reason = "";
        this.code = 0;
        this.timestamp = 0;
        this.data = null;
    }
}