package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.DanhMuc;
import com.example.van_phong_pham.repository.DanhMucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DanhMucService {
    @Autowired
    private DanhMucRepository danhMucRepository;

    public List<DanhMuc> getAllCategories() {
        return danhMucRepository.findAll(); // Lấy tất cả danh mục từ DB
    }

    public List<DanhMuc> getAll() {
        return danhMucRepository.findAll();
    }

    public DanhMuc getById(Integer id) {
        return danhMucRepository.findById(id).orElse(null);
    }
}