package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.HinhAnhSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HinhAnhSanPhamRepository extends JpaRepository<HinhAnhSanPham, Integer> {
    
    @Query("SELECT hasp FROM HinhAnhSanPham hasp WHERE hasp.sanPham.id_sanpham = :sanPhamId")
    List<HinhAnhSanPham> findBySanPhamId_sanpham(@Param("sanPhamId") Integer sanPhamId);

    @Query("SELECT hasp FROM HinhAnhSanPham hasp WHERE hasp.sanPham.id_sanpham = :sanPhamId AND hasp.la_anh_chinh = true")
    Optional<HinhAnhSanPham> findBySanPhamId_sanphamAndLa_anh_chinhTrue(@Param("sanPhamId") Integer sanPhamId);

    @Transactional
    @Modifying
    @Query("DELETE FROM HinhAnhSanPham hasp WHERE hasp.sanPham.id_sanpham = :sanPhamId")
    void deleteBySanPhamId_sanpham(@Param("sanPhamId") Integer sanPhamId);
}
