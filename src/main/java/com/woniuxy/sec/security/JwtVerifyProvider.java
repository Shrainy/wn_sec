package com.woniuxy.sec.security;

import cn.hutool.jwt.JWTUtil;
import com.woniuxy.sec.entity.Perm;
import com.woniuxy.sec.repository.PermRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Slf4j
public class JwtVerifyProvider implements AuthenticationProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Autowired
    private PermRepository permRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authentication;
        String jwt = jwtToken.getPrincipal().toString();
        boolean verify = false;
        try {
            verify = JWTUtil.verify(jwt, secretKey.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("JWT校验失败");
        }
        if (verify) {
            Integer adminId = Integer.parseInt(JWTUtil.parseToken(jwt).getPayload("id").toString());
            List<Perm> perms = permRepository.getPermsByAdminId(adminId);
            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>(perms.size());
            for (Perm perm : perms) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(perm.getCode());
                grantedAuthorities.add(grantedAuthority);
            }
            JwtToken newJwtToken = new JwtToken(jwt, grantedAuthorities);
            // 将带有权限信息的认证信息存储到全局作用域
            SecurityContextHolder.getContext().setAuthentication(newJwtToken);
            return newJwtToken;
        }
        throw new JwtVerifyException("JWT校验失败");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtToken.class.isAssignableFrom(authentication);
    }
}
