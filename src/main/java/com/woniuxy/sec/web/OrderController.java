package com.woniuxy.sec.web;

import com.woniuxy.sec.vo.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@RestController
@RequestMapping("api")
public class OrderController {

    @PreAuthorize("hasAuthority('order:list')")
    @GetMapping("order/list")
    public ResponseResult<Void> test(){
        return ResponseResult.ok();
    }
}
