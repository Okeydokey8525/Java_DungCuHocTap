package com.example.van_phong_pham.repository;


import com.example.van_phong_pham.model.DonHang;
import com.example.van_phong_pham.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonHangRepository  extends JpaRepository<DonHang, Integer> {

    List<DonHang> findByNguoiDung(NguoiDung nguoiDung);


}
