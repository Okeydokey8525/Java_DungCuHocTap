package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.DonHang;
import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.service.DonHangService;
import com.example.van_phong_pham.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final DonHangService donHangService;
    private final NguoiDungService nguoiDungService;

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "thanh-toan";
    }

    @PostMapping("/checkout/confirm")
    public String confirmCheckout(Principal principal, RedirectAttributes redirectAttributes) {
        NguoiDung user = nguoiDungService.findByEmail(principal.getName());

        DonHang donHang = new DonHang();
        donHang.setNguoiDung(user);
        donHang.setNgayDat(LocalDateTime.now());
        donHang.setTong_tien(0.0);
        donHang.setTrang_thai("Chờ xác nhận");
        donHangService.createOrder(donHang);

        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công. Bạn có thể theo dõi tại lịch sử mua hàng.");
        return "redirect:/orders";
    }
}
