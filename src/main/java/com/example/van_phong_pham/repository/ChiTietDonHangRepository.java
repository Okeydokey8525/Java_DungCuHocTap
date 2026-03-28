package com.example.van_phong_pham.repository;


import com.example.van_phong_pham.model.ChiTietDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietDonHangRepository  extends JpaRepository<ChiTietDonHang, Integer>{
}
