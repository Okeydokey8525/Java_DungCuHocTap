package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final NguoiDungService nguoiDungService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new NguoiDung());
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") NguoiDung user,
                           RedirectAttributes redirectAttributes) {
        NguoiDung existedUser = nguoiDungService.findByEmail(user.getEmail());
        if (existedUser != null) {
            redirectAttributes.addFlashAttribute("error", "Email đã được sử dụng.");
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/register";
        }

        nguoiDungService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công. Vui lòng đăng nhập.");
        return "redirect:/login";
    }
}
