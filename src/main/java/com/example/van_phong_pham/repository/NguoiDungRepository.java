package com.example.van_phong_pham.repository;

import com.example.van_phong_pham.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    // Bạn có thể thêm hàm tìm kiếm theo Email để phục vụ việc Đăng nhập
    NguoiDung findByEmail(String email);
}