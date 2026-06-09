package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Integer> {
    
    @Query("SELECT mgg FROM MaGiamGia mgg WHERE mgg.ma_code = :maCode")
    Optional<MaGiamGia> findByMa_code(@Param("maCode") String maCode);

    @Query("SELECT mgg FROM MaGiamGia mgg WHERE mgg.trang_thai = true")
    List<MaGiamGia> findByTrang_thaiTrue();
}
