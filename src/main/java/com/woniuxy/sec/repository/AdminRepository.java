package com.woniuxy.sec.repository;

import com.woniuxy.sec.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByAccount(String account);
}
