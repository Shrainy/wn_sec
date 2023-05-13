package com.woniuxy.sec.config;

import cn.hutool.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woniuxy.sec.security.*;
import com.woniuxy.sec.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain apiSsecurityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(apiAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtVerifyFilter(), ApiAuthenticationFilter.class);
        http
                .cors().configurationSource(configurationSource()) //配置跨域
                .and().antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/api/admin/login").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        http.exceptionHandling().accessDeniedHandler((req, resp, ex) -> {
            ex.printStackTrace();
            resp.setContentType("application/json;charset=UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            String r = objectMapper.writeValueAsString(new ResponseResult(601, "无权限"));
            resp.getWriter().write(r);
            resp.getWriter().close();
        });
        return http.build();
    }

    private CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setExposedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", corsConfiguration);
        return source;
    }

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() {
        try {
            return authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    public void setAuthenticationManagerBuilder(AuthenticationManagerBuilder builder) {
        builder
                .authenticationProvider(authenticationProvider())
                .authenticationProvider(jwtVerifyProvider())
                .authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public ApiAuthenticationProvider authenticationProvider() {
        return new ApiAuthenticationProvider();
    }

    @Bean
    public JwtVerifyProvider jwtVerifyProvider() {
        return new JwtVerifyProvider();
    }

    public ApiAuthenticationFilter apiAuthenticationFilter() {
        ApiAuthenticationFilter apiAuthenticationFilter = new ApiAuthenticationFilter();
        apiAuthenticationFilter.setAuthenticationManager(authenticationManager());
        apiAuthenticationFilter.setAuthenticationSuccessHandler((req, resp, auth) -> {
            ApiAuthenticationToken apiToken = (ApiAuthenticationToken) auth;
            String token = JWT.create()
                    .setPayload("id", String.valueOf(apiToken.getId()))
                    .setPayload("account", apiToken.getPrincipal())
                    .setKey(secretKey.getBytes())
                    .sign();
            resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
            resp.setHeader("Authorization", token);
            ObjectMapper objectMapper = new ObjectMapper();
            String r = objectMapper.writeValueAsString(ResponseResult.ok());
            resp.getWriter().write(r);
            resp.getWriter().close();
        });

        apiAuthenticationFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            String r = "";
            if (ex instanceof UsernameNotFoundException) {
                r = objectMapper.writeValueAsString(new ResponseResult(404, "账号不存在"));
            } else if (ex instanceof BadCredentialsException) {
                r = objectMapper.writeValueAsString(new ResponseResult(405, "密码错误"));
            } else {
                r = objectMapper.writeValueAsString(new ResponseResult(500, "未知错误"));
            }
            resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            resp.getWriter().write(r);
            resp.getWriter().close();
        });

        return apiAuthenticationFilter;
    }

    public JwtVerifyFilter jwtVerifyFilter() {
        JwtVerifyFilter jwtVerifyFilter = new JwtVerifyFilter("/api/**");
        jwtVerifyFilter.setAuthenticationManager(authenticationManager());
        jwtVerifyFilter.setAuthenticationFailureHandler((req, resp, ex) -> {
            ObjectMapper objectMapper = new ObjectMapper();
            String r = objectMapper.writeValueAsString(new ResponseResult(501, "JWT校验失败"));
            resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            resp.getWriter().write(r);
            resp.getWriter().close();
        });
        return jwtVerifyFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin() //表单登录
                .loginPage("/login")   //登录页面
                .loginProcessingUrl("/tologin")  //登录接口
                .usernameParameter("account")  //指定用户名参数
                .passwordParameter("password")//指定密码参数
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()  //登录页面直接放心
                .and()
                .authorizeRequests()
                .anyRequest().authenticated(); //要求认证
        http.csrf().disable(); //禁用csrf漏洞

        return http.build();

        //TODO 配置登录成功之后跳转的页面，
    }

    @Bean
    public SecUserDetailsService secUserDetailsService() {
        SecUserDetailsService secUserDetailsService = new SecUserDetailsService();

        return secUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(secUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
}
