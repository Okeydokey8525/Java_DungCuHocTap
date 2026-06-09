package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    
    NguoiDung findByEmail(String email);

    List<NguoiDung> findByVaiTro(VaiTro vaiTro);

    @Query("SELECT COUNT(nd) FROM NguoiDung nd WHERE nd.vaiTro.ten_vaitro = :roleName")
    long countByVaiTroTen_vaitro(@Param("roleName") String roleName);

    @Query("SELECT nd FROM NguoiDung nd WHERE nd.trang_thai = true")
    List<NguoiDung> findByTrang_thaiTrue();

    @Query("SELECT nd FROM NguoiDung nd WHERE nd.trang_thai = false")
    List<NguoiDung> findByTrang_thaiFalse();
}