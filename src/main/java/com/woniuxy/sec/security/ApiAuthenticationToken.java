package com.woniuxy.sec.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
public class ApiAuthenticationToken extends AbstractAuthenticationToken {
    private Integer id;
    private String account;
    private String password;

    public ApiAuthenticationToken(String account, String password, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.account = account;
        this.password = password;
        setAuthenticated(false);
    }

    public ApiAuthenticationToken(Integer id, String account, String password, Collection<? extends GrantedAuthority> authorities, boolean authenticatd) {
        super(authorities);
        this.id = id;
        this.account = account;
        this.password = password;
        setAuthenticated(authenticatd);
    }

    public ApiAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }

    public Integer getId(){
        return id;
    }
}
