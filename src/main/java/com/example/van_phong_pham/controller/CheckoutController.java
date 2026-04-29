package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.*;
import com.example.van_phong_pham.service.DonHangService;
import com.example.van_phong_pham.service.GioHangService;
import com.example.van_phong_pham.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final DonHangService donHangService;
    private final GioHangService gioHangService;
    private final NguoiDungService nguoiDungService;

    // Hiển thị trang thanh toán
    @GetMapping("/checkout")
    public String checkoutPage(Principal principal, Model model, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        NguoiDung user = nguoiDungService.findByEmail(principal.getName());
        var cartItems = gioHangService.getCartItems(user);
        
        // Kiểm tra giỏ hàng có trống không
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống. Vui lòng thêm sản phẩm trước khi thanh toán.");
            return "redirect:/cart";
        }

        Double cartTotal = gioHangService.getCartTotal(user);
        int cartCount = gioHangService.getCartItemCount(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("cartCount", cartCount);
        model.addAttribute("user", user);

        return "thanh-toan";
    }

    // Xác nhận thanh toán và tạo đơn hàng
    @PostMapping("/checkout/confirm")
    public String confirmCheckout(
            Principal principal,
            @RequestParam(required = false) String diaChiGiaoHang,
            @RequestParam(required = false) String ghiChu,
            @RequestParam(required = false, defaultValue = "cod") String phuongThucThanhToan,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            var cartItems = gioHangService.getCartItems(user);

            // Kiểm tra giỏ hàng có trống không
            if (cartItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống.");
                return "redirect:/cart";
            }

            // Tạo đơn hàng mới
            DonHang donHang = new DonHang();
            donHang.setNguoiDung(user);
            donHang.setNgayDat(LocalDateTime.now());
            donHang.setTrang_thai("Chờ xác nhận");

            // Tính tổng tiền
            Double tongTien = gioHangService.getCartTotal(user);
            donHang.setTong_tien(tongTien);

            // Lưu đơn hàng trước để lấy ID
            DonHang savedOrder = donHangService.createOrder(donHang);

            // Tạo chi tiết đơn hàng từ giỏ hàng
            List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();
            for (GioHang cartItem : cartItems) {
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setDonHang(savedOrder);
                chiTiet.setSanPham(cartItem.getSanPham());
                chiTiet.setSo_luong(cartItem.getSo_luong());
                chiTiet.setDon_gia(cartItem.getSanPham().getGia());
                chiTiet.setThanh_tien(cartItem.getSanPham().getGia() * cartItem.getSo_luong());
                chiTietDonHangs.add(chiTiet);
            }

            // Lưu chi tiết đơn hàng
            donHangService.saveOrder(savedOrder, chiTietDonHangs);

            // Xóa giỏ hàng sau khi đặt hàng thành công
            gioHangService.clearCart(user);

            redirectAttributes.addFlashAttribute("success", 
                "Đặt hàng thành công! Mã đơn hàng: #" + savedOrder.getId_donhang() + 
                ". Bạn có thể theo dõi tại lịch sử mua hàng.");
            
            return "redirect:/orders";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    // API để tính phí vận chuyển (nếu cần)
    @GetMapping("/checkout/shipping-fee")
    @ResponseBody
    public String calculateShippingFee(
            @RequestParam Double cartTotal,
            @RequestParam(required = false) String tinhThanh) {
        
        // Logic tính phí vận chuyển
        Double shippingFee = 0.0;
        
        if (cartTotal < 500000) { // Đơn hàng dưới 500k
            shippingFee = 30000.0;
        } else if (cartTotal < 1000000) { // Đơn hàng từ 500k - 1tr
            shippingFee = 20000.0;
        }
        // Đơn hàng từ 1tr miễn phí vận chuyển

        return shippingFee.toString();
    }

    // API để áp dụng mã giảm giá (nếu cần)
    @PostMapping("/checkout/discount")
    @ResponseBody
    public String applyDiscount(
            @RequestParam String discountCode,
            @RequestParam Double cartTotal) {
        
        // Logic kiểm tra và áp dụng mã giảm giá
        Double discountAmount = 0.0;
        
        // Ví dụ: Mã "SALE10" giảm 10%
        if ("SALE10".equals(discountCode)) {
            discountAmount = cartTotal * 0.1;
        }
        // Mã "FREESHIP" miễn phí vận chuyển
        else if ("FREESHIP".equals(discountCode)) {
            discountAmount = 30000.0; // Giả sử phí vận chuyển là 30k
        }

        return discountAmount.toString();
    }
}
