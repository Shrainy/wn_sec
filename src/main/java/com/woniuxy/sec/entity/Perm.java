package com.woniuxy.sec.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Data
@Entity
@Table(name = "sec_perm")
public class Perm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String code;
    private String url;
    private Integer pid;
}
