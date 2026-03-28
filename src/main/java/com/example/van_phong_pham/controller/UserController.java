package com.example.van_phong_pham.controller;


import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.service.DonHangService;
import com.example.van_phong_pham.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final NguoiDungService nguoiDungService;
    private final DonHangService donHangService;

    // Trang Hồ sơ cá nhân
    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        NguoiDung user = nguoiDungService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "ho-so";
    }

    // Trang Lịch sử mua hàng
    @GetMapping("/orders")
    public String history(Principal principal, Model model) {
        NguoiDung user = nguoiDungService.findByEmail(principal.getName());
        // Bạn cần viết thêm hàm findByNguoiDung trong DonHangService
        model.addAttribute("listDonHang", donHangService.getOrdersByUser(user));
        return "lich-su-mua-hang";
    }
}
