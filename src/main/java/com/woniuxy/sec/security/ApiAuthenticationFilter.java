package com.woniuxy.sec.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 用于登录
 *
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
public class ApiAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public ApiAuthenticationFilter() {
        super(new AntPathRequestMatcher("/api/admin/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> map = objectMapper.readValue(request.getInputStream(), Map.class);
        String account = map.get("account");
        String password = map.get("password");

        ApiAuthenticationToken apiAuthenticationToken = new ApiAuthenticationToken(account, password, null);

        return this.getAuthenticationManager().authenticate(apiAuthenticationToken);
    }
}
