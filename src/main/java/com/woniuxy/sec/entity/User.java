package com.woniuxy.sec.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Data
@Entity
@Table(name = "sec_user")
public class User {
    @Id   //修飾主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //主鍵生成策略
    private Integer id;
    @Column(name = "account")
    private String account;
    private String password;
}
