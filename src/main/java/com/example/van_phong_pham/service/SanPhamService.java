package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.SanPham;
import com.example.van_phong_pham.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // Search by keyword
    public List<SanPham> searchByName(String keyword) {
        return sanPhamRepository.findByTen_sanphamContainingIgnoreCase(keyword);
    }

    // Filter by category
    public List<SanPham> getByCategory(Integer danhMucId) {
        return sanPhamRepository.findByDanhMucId_danhmuc(danhMucId);
    }

    // Multi-filter with pagination
    public Page<SanPham> searchProducts(String keyword, Integer danhMucId,
            Double minPrice, Double maxPrice, String thuongHieu,
            int page, int size, String sortBy) {
        Sort sort = switch (sortBy != null ? sortBy : "newest") {
            case "price_asc" -> Sort.by("gia").ascending();
            case "price_desc" -> Sort.by("gia").descending();
            case "name" -> Sort.by("ten_sanpham").ascending();
            default -> Sort.by("ngayTao").descending(); // newest
        };
        Pageable pageable = PageRequest.of(page, size, sort);
        return sanPhamRepository.findByMultiFilter(keyword, danhMucId, minPrice, maxPrice, thuongHieu, pageable);
    }

    // Low stock products for admin warning
    public List<SanPham> getLowStockProducts() {
        return sanPhamRepository.findBySo_luongLessThan(5);
    }

    // Count active products
    public long countActiveProducts() {
        return sanPhamRepository.countByTrang_thaiTrue();
    }

    // Products on sale
    public List<SanPham> getSaleProducts() {
        return sanPhamRepository.findByGia_giamIsNotNull();
    }

    public long countProducts() {
        return sanPhamRepository.count();
    }
}
