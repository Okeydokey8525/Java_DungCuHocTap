package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.ChiTietDonHang;
import com.example.van_phong_pham.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Integer> {
    
    // Lấy danh sách chi tiết đơn hàng theo ID đơn hàng
    @Query("SELECT ct FROM ChiTietDonHang ct WHERE ct.donHang.id_donhang = :id_donhang")
    List<ChiTietDonHang> findByDonHangId_donhang(@Param("id_donhang") Integer id_donhang);
    
    // Xóa chi tiết đơn hàng theo ID đơn hàng
    @Modifying
    @Query("DELETE FROM ChiTietDonHang ct WHERE ct.donHang.id_donhang = :id_donhang")
    void deleteByDonHangId_donhang(@Param("id_donhang") Integer id_donhang);
    
    // Lấy chi tiết đơn hàng theo ID đơn hàng và ID sản phẩm
    @Query("SELECT ct FROM ChiTietDonHang ct WHERE ct.donHang.id_donhang = :id_donhang AND ct.sanPham.id_sanpham = :id_sanpham")
    ChiTietDonHang findByDonHangId_donhangAndSanPhamId_sanpham(@Param("id_donhang") Integer id_donhang, @Param("id_sanpham") Integer id_sanpham);

    // Kiểm tra người dùng đã mua sản phẩm và đơn hàng đã hoàn thành
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ChiTietDonHang c WHERE c.donHang.nguoiDung = :user AND c.donHang.trang_thai = 'Hoàn thành' AND c.sanPham.id_sanpham = :productId")
    boolean existsByUserAndProductAndStatusCompleted(@Param("user") NguoiDung user, @Param("productId") Integer productId);
}
