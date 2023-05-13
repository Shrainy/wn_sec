package com.woniuxy.sec.web;

import com.woniuxy.sec.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 老谭（<a href="http://www.woniuxy.com">蜗牛学苑</a>）
 */
@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("login")
    public String login() {
        return "user_login";
    }

//    @PostMapping("login")
//    public String login(String account, String password, Model model, HttpSession session) {
//        User user = userRepository.getByAccount(account);
//        if (user == null) {
//            model.addAttribute("msg", "账号不存在");
//            return "login";
//        } else {
//            if (!user.getPassword().equals(password)) {
//                model.addAttribute("msg", "密码不正确");
//                return "login";
//            } else {
//                session.setAttribute("user", user);
//                return "index";
//            }
//        }
//    }

}
