package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Integer> {
    
    @Query("SELECT vt FROM VaiTro vt WHERE vt.ten_vaitro = :tenVaitro")
    VaiTro findByTen_vaitro(@Param("tenVaitro") String tenVaitro);
}
