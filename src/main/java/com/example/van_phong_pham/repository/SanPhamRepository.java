package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    // Spring sẽ tự động tạo các hàm: save(), findAll(), findById(), delete()...
}
