package com.woniuxy.sec.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
public class JwtVerifyFilter extends AbstractAuthenticationProcessingFilter {
    public JwtVerifyFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
//    }
//
//    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//            attemptAuthentication(request, response);
//            chain.doFilter(request, response);
//        } catch (AuthenticationException e) {
//            throw e;
//        }
//    }


//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        try {
//            attemptAuthentication((HttpServletRequest) request, (HttpServletResponse) response);
//            chain.doFilter(request,response);
//        }catch (AuthenticationException a){
//            throw a;
//        }
//    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //从请求头中获取到JWT
        String jwt = request.getHeader("Authorization");
        JwtToken jwtToken = new JwtToken(jwt, null);
        return this.getAuthenticationManager().authenticate(jwtToken);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        chain.doFilter(request,response);
    }
}
