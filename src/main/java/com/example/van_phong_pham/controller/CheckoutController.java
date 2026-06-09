package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.*;
import com.example.van_phong_pham.service.DonHangService;
import com.example.van_phong_pham.service.GioHangService;
import com.example.van_phong_pham.service.NguoiDungService;
import com.example.van_phong_pham.service.SanPhamService;
import com.example.van_phong_pham.service.MaGiamGiaService;
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
    private final SanPhamService sanPhamService;
    private final MaGiamGiaService maGiamGiaService;

    // Hiển thị trang thanh toán
    @GetMapping("/checkout")
    public String checkoutPage(
            @RequestParam(required = false) List<Integer> items,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        NguoiDung user = nguoiDungService.findByEmail(principal.getName());
        var allCartItems = gioHangService.getCartItems(user);
        List<GioHang> cartItems = new ArrayList<>();

        if (items != null && !items.isEmpty()) {
            for (GioHang cartItem : allCartItems) {
                if (items.contains(cartItem.getSanPham().getId_sanpham())) {
                    cartItems.add(cartItem);
                }
            }
        } else {
            // Nếu không truyền items, thanh toán toàn bộ giỏ hàng
            cartItems = allCartItems;
        }
        
        // Kiểm tra giỏ hàng có trống không
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ít nhất một sản phẩm để thanh toán.");
            return "redirect:/cart";
        }

        // Tính tổng tiền dựa trên các sản phẩm được chọn
        Double cartTotal = 0.0;
        for (GioHang cartItem : cartItems) {
            Double price = cartItem.getSanPham().getGia();
            if (cartItem.getSanPham().getGia_giam() != null) {
                price = cartItem.getSanPham().getGia_giam();
            }
            cartTotal += price * cartItem.getSo_luong();
        }
        int cartCount = cartItems.size();

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
            @RequestParam(required = false) List<Integer> items,
            @RequestParam(required = false) String diaChiGiaoHang,
            @RequestParam(required = false) String ghiChu,
            @RequestParam(required = false, defaultValue = "cod") String phuongThucThanhToan,
            @RequestParam(required = false) String discountCode,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            var allCartItems = gioHangService.getCartItems(user);
            List<GioHang> cartItems = new ArrayList<>();

            if (items != null && !items.isEmpty()) {
                for (GioHang cartItem : allCartItems) {
                    if (items.contains(cartItem.getSanPham().getId_sanpham())) {
                        cartItems.add(cartItem);
                    }
                }
            } else {
                cartItems = allCartItems;
            }

            // Kiểm tra giỏ hàng có trống không
            if (cartItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ít nhất một sản phẩm để thanh toán.");
                return "redirect:/cart";
            }

            // Kiểm tra tồn kho trước khi đặt hàng
            for (GioHang cartItem : cartItems) {
                SanPham sp = cartItem.getSanPham();
                if (sp.getSo_luong() != null && cartItem.getSo_luong() > sp.getSo_luong()) {
                    redirectAttributes.addFlashAttribute("error", 
                        "Sản phẩm '" + sp.getTen_sanpham() + "' chỉ còn " + sp.getSo_luong() + " sản phẩm. Vui lòng cập nhật giỏ hàng.");
                    return "redirect:/cart";
                }
            }

            // Tạo đơn hàng mới
            DonHang donHang = new DonHang();
            donHang.setNguoiDung(user);
            donHang.setNgayDat(LocalDateTime.now());
            donHang.setTrang_thai(phuongThucThanhToan.equalsIgnoreCase("banking") ? "Chờ thanh toán" : "Chờ xác nhận");
            donHang.setDiaChiGiaoHang(diaChiGiaoHang);
            donHang.setGhi_chu(ghiChu);
            donHang.setPhuongThucThanhToan(phuongThucThanhToan.toUpperCase());

            // Tính tổng tiền giỏ hàng của các mặt hàng được chọn
            Double cartTotal = 0.0;
            for (GioHang cartItem : cartItems) {
                Double price = cartItem.getSanPham().getGia();
                if (cartItem.getSanPham().getGia_giam() != null) {
                    price = cartItem.getSanPham().getGia_giam();
                }
                cartTotal += price * cartItem.getSo_luong();
            }

            // Tính phí vận chuyển (Ví dụ đơn dưới 500k phí 30k, dưới 1 triệu phí 20k, từ 1tr freeship)
            Double shippingFee = 0.0;
            if (cartTotal < 500000) {
                shippingFee = 30000.0;
            } else if (cartTotal < 1000000) {
                shippingFee = 20000.0;
            }
            donHang.setPhi_van_chuyen(shippingFee);

            // Áp dụng mã giảm giá thực tế nếu có
            Double discount = 0.0;
            if (discountCode != null && !discountCode.trim().isEmpty()) {
                discount = maGiamGiaService.validateAndApply(discountCode, cartTotal);
                if (discount > 0.0) {
                    maGiamGiaService.incrementUsage(discountCode);
                }
            }

            // Tính tổng tiền đơn hàng = tiền hàng + ship - giảm giá
            Double tongTien = cartTotal + shippingFee - discount;
            donHang.setTong_tien(tongTien);

            // Tạo danh sách chi tiết đơn hàng (chưa lưu)
            List<ChiTietDonHang> chiTietDonHangs = new ArrayList<>();
            for (GioHang cartItem : cartItems) {
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setSanPham(cartItem.getSanPham());
                chiTiet.setSo_luong(cartItem.getSo_luong());
                
                // Lấy đơn giá thực tế (có giảm giá sản phẩm không)
                Double donGia = cartItem.getSanPham().getGia();
                if (cartItem.getSanPham().getGia_giam() != null) {
                    donGia = cartItem.getSanPham().getGia_giam();
                }
                chiTiet.setDon_gia(donGia);
                chiTiet.setThanh_tien(donGia * cartItem.getSo_luong());
                chiTietDonHangs.add(chiTiet);
            }

            // Lưu đơn hàng và chi tiết trong một Transaction
            DonHang savedOrder = donHangService.saveOrder(donHang, chiTietDonHangs);

            // Trừ tồn kho sau khi lưu đơn thành công
            for (GioHang cartItem : cartItems) {
                SanPham sp = cartItem.getSanPham();
                if (sp.getSo_luong() != null) {
                    sp.setSo_luong(sp.getSo_luong() - cartItem.getSo_luong());
                    sanPhamService.saveProduct(sp);
                }
            }

            // Xóa các sản phẩm đã mua khỏi giỏ hàng
            for (GioHang cartItem : cartItems) {
                gioHangService.removeFromCart(user, cartItem.getSanPham().getId_sanpham());
            }

            if (phuongThucThanhToan.equalsIgnoreCase("banking")) {
                return "redirect:/checkout/payment-transfer/" + savedOrder.getId_donhang();
            }

            redirectAttributes.addFlashAttribute("success", 
                "Đặt hàng thành công! Mã đơn hàng: #" + savedOrder.getId_donhang() + 
                ". Bạn có thể theo dõi tại lịch sử mua hàng.");
            
            return "redirect:/orders";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    // API để tính phí vận chuyển
    @GetMapping("/checkout/shipping-fee")
    @ResponseBody
    public String calculateShippingFee(
            @RequestParam Double cartTotal,
            @RequestParam(required = false) String tinhThanh) {
        
        Double shippingFee = 0.0;
        if (cartTotal < 500000) {
            shippingFee = 30000.0;
        } else if (cartTotal < 1000000) {
            shippingFee = 20000.0;
        }
        return shippingFee.toString();
    }

    // API để áp dụng mã giảm giá thực tế
    @PostMapping("/checkout/discount")
    @ResponseBody
    public String applyDiscount(
            @RequestParam String discountCode,
            @RequestParam Double cartTotal) {
        
        Double discountAmount = maGiamGiaService.validateAndApply(discountCode, cartTotal);
        return discountAmount.toString();
    }

    // Hiển thị thông tin chuyển khoản ngân hàng
    @GetMapping("/checkout/payment-transfer/{orderId}")
    public String paymentTransferPage(
            @PathVariable Integer orderId,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (principal == null) {
            return "redirect:/login";
        }

        NguoiDung user = nguoiDungService.findByEmail(principal.getName());
        DonHang order = donHangService.getOrderById(orderId);

        if (order == null || !order.getNguoiDung().getId_nguoidung().equals(user.getId_nguoidung())) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại hoặc không thuộc quyền sở hữu của bạn.");
            return "redirect:/home";
        }

        if (!"CHỜ THANH TOÁN".equalsIgnoreCase(order.getTrang_thai())) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng này không ở trạng thái chờ thanh toán.");
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "checkout/payment-transfer";
    }

    // Xác nhận đã thanh toán chuyển khoản
    @PostMapping("/checkout/payment-transfer/{orderId}/confirm")
    public String confirmPaymentTransfer(
            @PathVariable Integer orderId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        NguoiDung user = nguoiDungService.findByEmail(principal.getName());
        DonHang order = donHangService.getOrderById(orderId);

        if (order == null || !order.getNguoiDung().getId_nguoidung().equals(user.getId_nguoidung())) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại.");
            return "redirect:/home";
        }

        if (!"CHỜ THANH TOÁN".equalsIgnoreCase(order.getTrang_thai())) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng này không ở trạng thái chờ thanh toán.");
            return "redirect:/orders";
        }

        // Cập nhật trạng thái thành Chờ xác nhận
        donHangService.updateOrderStatus(orderId, "Chờ xác nhận");

        redirectAttributes.addFlashAttribute("success", 
            "Xác nhận đã chuyển khoản thành công! Đơn hàng #" + orderId + " đang chờ ban quản trị duyệt.");
        return "redirect:/orders";
    }
}
