package com.example.van_phong_pham.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login"; // Mở file login.html trong thư mục templates
    }

    @GetMapping("/home")
    public String home() {
        return "home"; // Trang chủ sau khi đăng nhập thành công
    }
}
