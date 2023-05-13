package com.woniuxy.sec.vo;

import lombok.Data;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Data
public class ResponseResult<T> {
    private int code;
    public String msg;
    private T data;

    public ResponseResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(T data) {
        this(200, "ok", data);
    }

    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResponseResult<Void> ok() {
        return new ResponseResult<>(200, "ok");
    }

}
