package com.smlj.dailypaper.utils;

import com.smlj.dailypaper.proto.to.common.Result;

// 前后端交互数据标准
public final class ResultUtil<T> {
    private final Result<T> result;

    public ResultUtil() {
        this.result = new Result<T>();
        this.result.setResult(true);
        this.result.setReason("");
        this.result.setCode(200);
    }

    public void empty(){
        this.result.empty();
    }

    public static <T> Result<T> data(T t) {
        return new ResultUtil<T>().setData(t);
    }

    public static <T> Result<T> data(T t, String msg) {
        return new ResultUtil<T>().setData(t, msg);
    }

    public static <T> Result<T> success(String msg, T t) {
        return new ResultUtil<T>().setSuccessMsg(msg, t);
    }

    public static <T> Result<T> error(String msg, T t) {
        return new ResultUtil<T>().setErrorMsg(msg, t);
    }

    public static <T> Result<T> error(int code, String msg, T t) {
        return new ResultUtil<T>().setErrorMsg(code, msg, t);
    }

    public Result<T> setData(T t) {
        this.result.setData(t);
        return this.result;
    }

    public Result<T> setData(T t, String msg) {
        this.result.setData(t);
        this.result.setReason(msg);
        return this.result;
    }

    public Result<T> setSuccessMsg(String msg, T t) {
        this.result.setResult(true);
        this.result.setReason(msg);
        this.result.setCode(200);
        this.result.setData(t);
        return this.result;
    }

    public Result<T> setErrorMsg(String msg, T t) {
        this.result.setResult(false);
        this.result.setReason(msg);
        this.result.setCode(500);
        this.result.setData(t);
        return this.result;
    }

    public Result<T> setErrorMsg(int code, String msg, T t) {
        this.result.setResult(false);
        this.result.setReason(msg);
        this.result.setCode(code);
        this.result.setData(t);
        return this.result;
    }
}