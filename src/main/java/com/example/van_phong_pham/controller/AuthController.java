package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.VaiTro;
import com.example.van_phong_pham.service.NguoiDungService;
import com.example.van_phong_pham.service.VaiTroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final NguoiDungService nguoiDungService;
    private final VaiTroService vaiTroService;

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

        // Gán vai trò ROLE_USER mặc định
        VaiTro userRole = vaiTroService.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new VaiTro();
            userRole.setTen_vaitro("ROLE_USER");
            userRole = vaiTroService.save(userRole);
        }
        user.setVaiTro(userRole);

        nguoiDungService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công. Vui lòng đăng nhập.");
        return "redirect:/login";
    }

    @GetMapping("/quen-mat-khau")
    public String showForgotPasswordForm() {
        return "quen-mat-khau";
    }

    @PostMapping("/quen-mat-khau")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp.");
            return "redirect:/quen-mat-khau";
        }
        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự.");
            return "redirect:/quen-mat-khau";
        }
        NguoiDung user = nguoiDungService.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại trong hệ thống.");
            return "redirect:/quen-mat-khau";
        }
        nguoiDungService.changePassword(user, newPassword);
        redirectAttributes.addFlashAttribute("success", "Đặt lại mật khẩu thành công. Vui lòng đăng nhập.");
        return "redirect:/login";
    }
}
