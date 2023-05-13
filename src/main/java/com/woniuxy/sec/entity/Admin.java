package com.woniuxy.sec.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Data
@Entity
@Table(name = "sec_admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String account;
    private String password;
}
