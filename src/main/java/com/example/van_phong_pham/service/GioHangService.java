package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.GioHang;
import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.SanPham;
import com.example.van_phong_pham.repository.GioHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GioHangService {

    private final GioHangRepository gioHangRepository;
    private final SanPhamService sanPhamService;

    // Lấy tất cả items trong giỏ hàng của người dùng
    public List<GioHang> getCartItems(NguoiDung nguoiDung) {
        return gioHangRepository.findByNguoiDung(nguoiDung);
    }

    // Thêm sản phẩm vào giỏ hàng
    public GioHang addToCart(NguoiDung nguoiDung, Integer sanPhamId, Integer soLuong) {
        // Lấy thông tin sản phẩm
        SanPham sanPham = sanPhamService.findById(sanPhamId);
        if (sanPham == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        Optional<GioHang> existingItem = gioHangRepository.findByNguoiDungAndSanPham(nguoiDung, sanPham);

        if (existingItem.isPresent()) {
            // Nếu đã có, tăng số lượng
            GioHang cartItem = existingItem.get();
            cartItem.setSo_luong(cartItem.getSo_luong() + (soLuong != null ? soLuong : 1));
            cartItem.setNgayCapNhat(LocalDateTime.now());
            return gioHangRepository.save(cartItem);
        } else {
            // Nếu chưa có, tạo mới
            GioHang cartItem = new GioHang(nguoiDung, sanPham, soLuong != null ? soLuong : 1);
            return gioHangRepository.save(cartItem);
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public GioHang updateCartItem(NguoiDung nguoiDung, Integer sanPhamId, Integer soLuong) {
        SanPham sanPham = sanPhamService.findById(sanPhamId);
        if (sanPham == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        Optional<GioHang> cartItem = gioHangRepository.findByNguoiDungAndSanPham(nguoiDung, sanPham);
        if (cartItem.isEmpty()) {
            throw new RuntimeException("Sản phẩm không có trong giỏ hàng");
        }

        GioHang item = cartItem.get();
        if (soLuong <= 0) {
            // Nếu số lượng <= 0, xóa item
            gioHangRepository.delete(item);
            return null;
        } else {
            // Cập nhật số lượng
            item.setSo_luong(soLuong);
            item.setNgayCapNhat(LocalDateTime.now());
            return gioHangRepository.save(item);
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeFromCart(NguoiDung nguoiDung, Integer sanPhamId) {
        SanPham sanPham = sanPhamService.findById(sanPhamId);
        if (sanPham == null) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }

        Optional<GioHang> cartItem = gioHangRepository.findByNguoiDungAndSanPham(nguoiDung, sanPham);
        if (cartItem.isPresent()) {
            gioHangRepository.delete(cartItem.get());
        }
    }

    // Xóa tất cả items trong giỏ hàng
    public void clearCart(NguoiDung nguoiDung) {
        gioHangRepository.deleteByNguoiDung(nguoiDung);
    }

    // Đếm số lượng items trong giỏ hàng
    public int getCartItemCount(NguoiDung nguoiDung) {
        return gioHangRepository.countByNguoiDung(nguoiDung);
    }

    // Tính tổng tiền giỏ hàng
    public Double getCartTotal(NguoiDung nguoiDung) {
        List<GioHang> cartItems = getCartItems(nguoiDung);
        return cartItems.stream()
                .mapToDouble(item -> item.getSanPham().getGia() * item.getSo_luong())
                .sum();
    }

    // Kiểm tra sản phẩm có trong giỏ hàng không
    public boolean isProductInCart(NguoiDung nguoiDung, Integer sanPhamId) {
        SanPham sanPham = sanPhamService.findById(sanPhamId);
        if (sanPham == null) {
            return false;
        }
        return gioHangRepository.existsByNguoiDungAndSanPham(nguoiDung, sanPham);
    }
}
