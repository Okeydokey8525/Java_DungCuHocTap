package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.DanhGia;
import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
    
    @Query("SELECT dg FROM DanhGia dg WHERE dg.sanPham.id_sanpham = :sanPhamId")
    List<DanhGia> findBySanPhamId_sanpham(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT dg FROM DanhGia dg WHERE dg.sanPham = :sanPham AND dg.trang_thai = true")
    List<DanhGia> findBySanPhamAndTrang_thaiTrue(@Param("sanPham") SanPham sanPham);

    List<DanhGia> findByNguoiDung(NguoiDung nguoiDung);

    @Query("SELECT dg FROM DanhGia dg WHERE dg.trang_thai = false")
    List<DanhGia> findByTrang_thaiFalse(); // pending approval

    @Query("SELECT CASE WHEN COUNT(dg) > 0 THEN true ELSE false END FROM DanhGia dg WHERE dg.nguoiDung = :nguoiDung AND dg.sanPham = :sanPham")
    boolean existsByNguoiDungAndSanPham(@Param("nguoiDung") NguoiDung nguoiDung, @Param("sanPham") SanPham sanPham);

    @Query("SELECT AVG(CAST(dg.so_sao AS double)) FROM DanhGia dg WHERE dg.sanPham.id_sanpham = :spId AND dg.trang_thai = true")
    Double getAverageRating(@Param("spId") Integer sanPhamId);

    @Query("SELECT COUNT(dg) FROM DanhGia dg WHERE dg.sanPham.id_sanpham = :sanPhamId")
    long countBySanPhamId_sanpham(@Param("sanPhamId") Integer sanPhamId);
}
