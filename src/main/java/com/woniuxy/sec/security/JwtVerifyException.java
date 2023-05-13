package com.woniuxy.sec.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
public class JwtVerifyException extends AuthenticationException {
    public JwtVerifyException(String msg) {
        super(msg);
    }

    public JwtVerifyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
