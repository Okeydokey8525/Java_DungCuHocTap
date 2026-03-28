package com.example.van_phong_pham.repository;


import com.example.van_phong_pham.model.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroRepository  extends JpaRepository<VaiTro, Integer> {
}
