package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.service.GioHangService;
import com.example.van_phong_pham.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final GioHangService gioHangService;
    private final NguoiDungService nguoiDungService;

    // Hiển thị trang giỏ hàng
    @GetMapping("/cart")
    public String cartPage(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        NguoiDung user = nguoiDungService.findByEmail(principal.getName());
        var cartItems = gioHangService.getCartItems(user);
        Double cartTotal = gioHangService.getCartTotal(user);
        int cartCount = gioHangService.getCartItemCount(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("cartCount", cartCount);

        return "gio-hang";
    }

    // Thêm sản phẩm vào giỏ hàng (AJAX)
    @PostMapping("/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Integer sanPhamId,
            @RequestParam(defaultValue = "1") Integer soLuong,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng");
            return ResponseEntity.ok(response);
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            gioHangService.addToCart(user, sanPhamId, soLuong);
            
            int cartCount = gioHangService.getCartItemCount(user);
            Double cartTotal = gioHangService.getCartTotal(user);

            response.put("success", true);
            response.put("message", "Đã thêm sản phẩm vào giỏ hàng");
            response.put("cartCount", cartCount);
            response.put("cartTotal", cartTotal);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/cart/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCart(
            @RequestParam Integer sanPhamId,
            @RequestParam Integer soLuong,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập");
            return ResponseEntity.ok(response);
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            gioHangService.updateCartItem(user, sanPhamId, soLuong);
            
            int cartCount = gioHangService.getCartItemCount(user);
            Double cartTotal = gioHangService.getCartTotal(user);

            response.put("success", true);
            response.put("message", "Đã cập nhật giỏ hàng");
            response.put("cartCount", cartCount);
            response.put("cartTotal", cartTotal);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/cart/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @RequestParam Integer sanPhamId,
            Principal principal) {

        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập");
            return ResponseEntity.ok(response);
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            gioHangService.removeFromCart(user, sanPhamId);
            
            int cartCount = gioHangService.getCartItemCount(user);
            Double cartTotal = gioHangService.getCartTotal(user);

            response.put("success", true);
            response.put("message", "Đã xóa sản phẩm khỏi giỏ hàng");
            response.put("cartCount", cartCount);
            response.put("cartTotal", cartTotal);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Xóa tất cả sản phẩm trong giỏ hàng
    @PostMapping("/cart/clear")
    public String clearCart(Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            gioHangService.clearCart(user);
            redirectAttributes.addFlashAttribute("success", "Đã xóa tất cả sản phẩm khỏi giỏ hàng");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/cart";
    }

    // Lấy thông tin giỏ hàng (AJAX - cho header)
    @GetMapping("/cart/info")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartInfo(Principal principal) {
        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("cartCount", 0);
            response.put("cartTotal", 0);
            return ResponseEntity.ok(response);
        }

        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            int cartCount = gioHangService.getCartItemCount(user);
            Double cartTotal = gioHangService.getCartTotal(user);

            response.put("cartCount", cartCount);
            response.put("cartTotal", cartTotal);

        } catch (Exception e) {
            response.put("cartCount", 0);
            response.put("cartTotal", 0);
        }

        return ResponseEntity.ok(response);
    }
}
