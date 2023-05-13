package com.woniuxy.sec.repository;

import com.woniuxy.sec.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("from User where account=?1")
    User getByAccount(String account);


}
