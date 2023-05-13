package com.woniuxy.sec.security;

import com.woniuxy.sec.entity.User;
import com.woniuxy.sec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
public class SecUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userRepository.getByAccount(account);
        if (user == null) {
            throw new UsernameNotFoundException("账号：" + account + "不存在");
        }
        return new SecUser(user.getId(), account, user.getPassword(), new ArrayList<>());
    }
}
