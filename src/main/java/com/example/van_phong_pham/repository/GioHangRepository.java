package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.GioHang;
import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    
    // Tìm tất cả items trong giỏ hàng của một người dùng
    List<GioHang> findByNguoiDung(NguoiDung nguoiDung);
    
    // Tìm item cụ thể trong giỏ hàng (người dùng + sản phẩm)
    Optional<GioHang> findByNguoiDungAndSanPham(NguoiDung nguoiDung, SanPham sanPham);
    
    // Đếm số lượng items trong giỏ hàng của người dùng
    int countByNguoiDung(NguoiDung nguoiDung);
    
    // Xóa tất cả items trong giỏ hàng của người dùng
    void deleteByNguoiDung(NguoiDung nguoiDung);
    
    // Kiểm tra sản phẩm có trong giỏ hàng của người dùng không
    boolean existsByNguoiDungAndSanPham(NguoiDung nguoiDung, SanPham sanPham);
}
