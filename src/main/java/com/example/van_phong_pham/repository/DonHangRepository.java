package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.DonHang;
import com.example.van_phong_pham.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

    List<DonHang> findByNguoiDung(NguoiDung nguoiDung);
    
    // Đếm số đơn hàng của người dùng
    long countByNguoiDung(NguoiDung nguoiDung);
    
    // Lấy đơn hàng theo trạng thái
    @Query("SELECT d FROM DonHang d WHERE d.trang_thai = :trang_thai")
    List<DonHang> findByTrang_thai(@Param("trang_thai") String trang_thai);
    
    // Lấy đơn hàng của người dùng theo trạng thái
    @Query("SELECT d FROM DonHang d WHERE d.nguoiDung = :nguoiDung AND d.trang_thai = :trang_thai")
    List<DonHang> findByNguoiDungAndTrang_thai(@Param("nguoiDung") NguoiDung nguoiDung, @Param("trang_thai") String trang_thai);

    // Đếm số đơn hàng theo trạng thái
    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.trang_thai = :trangThai")
    long countByTrang_thai(@Param("trangThai") String trangThai);

    // Tính tổng doanh thu từ các đơn hàng hoàn thành
    @Query("SELECT COALESCE(SUM(d.tong_tien), 0.0) FROM DonHang d WHERE d.trang_thai = 'Hoàn thành'")
    Double getTotalRevenue();

    // 10 đơn hàng gần nhất
    List<DonHang> findTop10ByOrderByNgayDatDesc();
}
