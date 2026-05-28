package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.service.DonHangService;
import com.example.van_phong_pham.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final NguoiDungService nguoiDungService;
    private final DonHangService donHangService;
    private final BCryptPasswordEncoder passwordEncoder;

    // Trang Hồ sơ cá nhân
    @GetMapping("/profile")
    public String profile(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xem hồ sơ");
            return "redirect:/login";
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            model.addAttribute("user", user);
            return "ho-so";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin người dùng");
            return "redirect:/login";
        }
    }

    // Cập nhật thông tin hồ sơ
    @PostMapping("/profile/update")
    public String updateProfile(Principal principal,
                               @RequestParam String ho_ten,
                               @RequestParam String so_dien_thoai,
                               @RequestParam(required = false) String dia_chi,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            user.setHo_ten(ho_ten);
            user.setSo_dien_thoai(so_dien_thoai);
            user.setDia_chi(dia_chi);
            nguoiDungService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể cập nhật thông tin: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    // Đổi mật khẩu
    @PostMapping("/profile/change-password")
    public String changePassword(Principal principal,
                                @RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            
            // Verify current password
            if (!passwordEncoder.matches(currentPassword, user.getMat_khau())) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu hiện tại không đúng.");
                return "redirect:/profile";
            }
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới xác nhận không khớp.");
                return "redirect:/profile";
            }
            if (newPassword.length() < 6) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự.");
                return "redirect:/profile";
            }
            
            nguoiDungService.changePassword(user, newPassword);
            redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi đổi mật khẩu: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    // Trang Lịch sử mua hàng
    @GetMapping("/orders")
    public String history(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để xem lịch sử mua hàng");
            return "redirect:/login";
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            model.addAttribute("user", user);
            model.addAttribute("listDonHang", donHangService.getOrdersByUser(user));
            return "lich-su-mua-hang";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải lịch sử mua hàng");
            return "redirect:/home";
        }
    }

    // Chi tiết đơn hàng
    @GetMapping("/orders/detail")
    public String orderDetail(
            @RequestParam Integer orderId,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập");
            return "redirect:/login";
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            var order = donHangService.getOrderById(orderId);
            
            // Kiểm tra xem đơn hàng có thuộc về user này không
            if (order == null || !order.getNguoiDung().getId_nguoidung().equals(user.getId_nguoidung())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
                return "redirect:/orders";
            }
            
            model.addAttribute("user", user);
            model.addAttribute("order", order);
            model.addAttribute("orderDetails", donHangService.getOrderDetails(orderId));
            return "chi-tiet-don-hang";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể tải chi tiết đơn hàng");
            return "redirect:/orders";
        }
    }

    // Hủy đơn hàng (chỉ cho đơn hàng ở trạng thái "Chờ xác nhận")
    @PostMapping("/orders/cancel")
    public String cancelOrder(
            @RequestParam Integer orderId,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập");
            return "redirect:/login";
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            var order = donHangService.getOrderById(orderId);
            
            // Kiểm tra xem đơn hàng có thuộc về user này và có thể hủy không
            if (order == null || !order.getNguoiDung().getId_nguoidung().equals(user.getId_nguoidung())) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng");
                return "redirect:/orders";
            }
            
            if (!"Chờ xác nhận".equalsIgnoreCase(order.getTrang_thai()) && !"Chờ thanh toán".equalsIgnoreCase(order.getTrang_thai())) {
                redirectAttributes.addFlashAttribute("error", "Đơn hàng này không thể hủy");
                return "redirect:/orders";
            }
            
            // Cập nhật trạng thái đơn hàng
            order.setTrang_thai("Đã hủy");
            donHangService.updateOrder(order);
            
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng thành công");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng");
        }
        
        return "redirect:/orders";
    }
}
