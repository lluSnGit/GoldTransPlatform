package com.mall.common.api;

import lombok.Data;

@Data
public class CommonResult<T> {
    private long code;
    private String message;
    private T data;

    protected CommonResult() {}

    protected CommonResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(200, "操作成功", data);
    }

    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(500, message, null);
    }
} 