package com.woniuxy.sec.security;

import com.woniuxy.sec.entity.Admin;
import com.woniuxy.sec.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
public class ApiAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiAuthenticationToken token = (ApiAuthenticationToken) authentication;
        //UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        Admin admin = adminRepository.findByAccount(token.getPrincipal().toString());
        if (admin == null) {
            throw new UsernameNotFoundException("账号" + token.getPrincipal() + "不存在");
        }
        //对比密码
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("密码为空");
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(presentedPassword, admin.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }
        return new ApiAuthenticationToken(admin.getId(), admin.getAccount(), admin.getPassword(), null, true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
