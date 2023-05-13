package com.woniuxy.sec.web;

import cn.hutool.jwt.JWT;
import com.woniuxy.sec.entity.Admin;
import com.woniuxy.sec.repository.AdminRepository;
import com.woniuxy.sec.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@CrossOrigin(origins = "*", exposedHeaders = "*")
@RestController
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @PostMapping("/admin/login")
    public ResponseResult<Void> login(@RequestBody Admin admin, HttpServletResponse response) {
        Admin a = adminRepository.findByAccount(admin.getAccount());
        if (a == null) {
            return new ResponseResult<>(404, "no user");
        } else {
            if (a.getPassword().equals(admin.getPassword())) {
                //产生JWT，存在响应头
                String token = JWT.create()
                        .setPayload("id", String.valueOf(admin.getId()))
                        .setPayload("account", admin.getAccount())
                        .setKey(secretKey.getBytes())
                        .sign();
                response.setHeader("Authorization", token);
                return ResponseResult.ok();
            } else {
                return new ResponseResult<>(406, "wrong password");
            }
        }
    }
}
