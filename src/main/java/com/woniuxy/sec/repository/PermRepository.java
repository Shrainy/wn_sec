package com.woniuxy.sec.repository;

import com.woniuxy.sec.entity.Perm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Repository
public interface PermRepository extends JpaRepository<Perm, Integer> {
    @Query(value = "select * from sec_perm p,sec_admin_perm ap " +
            "where p.id=ap.perm_id and ap.admin_id=?1", nativeQuery = true)
    List<Perm> getPermsByAdminId(Integer adminId);
}
