package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.ChiTietDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {
    
    // Lấy danh sách chi tiết đơn hàng theo ID đơn hàng
    List<ChiTietDonHang> findByDonHangId_donhang(Integer id_donhang);
    
    // Xóa chi tiết đơn hàng theo ID đơn hàng
    @Modifying
    @Query("DELETE FROM ChiTietDonHang ct WHERE ct.donHang.id_donhang = :id_donhang")
    void deleteByDonHangId_donhang(@Param("id_donhang") Integer id_donhang);
    
    // Lấy chi tiết đơn hàng theo ID đơn hàng và ID sản phẩm
    ChiTietDonHang findByDonHangId_donhangAndSanPhamId_sanpham(Integer id_donhang, Integer id_sanpham);
}
