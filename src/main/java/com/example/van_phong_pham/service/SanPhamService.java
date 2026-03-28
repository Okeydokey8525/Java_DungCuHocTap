package com.example.van_phong_pham.service;


import com.example.van_phong_pham.model.SanPham;
import com.example.van_phong_pham.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Đánh dấu đây là tầng Service để Spring quản lý
public class SanPhamService {

    @Autowired // Tự động kết nối với Repository tương ứng
    private SanPhamRepository sanPhamRepository;

    // 1. Hàm lấy tất cả sản phẩm (Để hiện lên trang chủ)
    public List<SanPham> getAllProducts() {
        return sanPhamRepository.findAll();
    }

    // 2. Hàm lấy 1 sản phẩm theo ID (Để hiện trang chi tiết)
    public SanPham getProductById(Integer id) {
        return sanPhamRepository.findById(id).orElse(null);
    }

    // 3. Hàm lưu sản phẩm (Để Admin thêm mới/sửa)
    public void saveProduct(SanPham sp) {
        sanPhamRepository.save(sp);
    }

    // 4. Hàm xóa sản phẩm
    public void deleteProduct(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    public SanPham findById(Integer id) {
        return sanPhamRepository.findById(id).orElse(null);
    }
}
