package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.DanhGia;
import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.SanPham;
import com.example.van_phong_pham.repository.DanhGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanhGiaService {
    @Autowired
    private DanhGiaRepository danhGiaRepository;

    public List<DanhGia> getReviewsByProduct(Integer sanPhamId) {
        return danhGiaRepository.findBySanPhamId_sanpham(sanPhamId);
    }

    public List<DanhGia> getApprovedReviews(SanPham sanPham) {
        return danhGiaRepository.findBySanPhamAndTrang_thaiTrue(sanPham);
    }

    public List<DanhGia> getPendingReviews() {
        return danhGiaRepository.findByTrang_thaiFalse();
    }

    public List<DanhGia> getAllReviews() {
        return danhGiaRepository.findAll();
    }

    public void saveReview(DanhGia danhGia) {
        danhGiaRepository.save(danhGia);
    }

    public void deleteReview(Integer id) {
        danhGiaRepository.deleteById(id);
    }

    public DanhGia findById(Integer id) {
        return danhGiaRepository.findById(id).orElse(null);
    }

    public void toggleStatus(Integer id) {
        DanhGia dg = findById(id);
        if (dg != null) {
            dg.setTrang_thai(dg.getTrang_thai() != null ? !dg.getTrang_thai() : true);
            danhGiaRepository.save(dg);
        }
    }

    public Double getAverageRating(Integer sanPhamId) {
        Double avg = danhGiaRepository.getAverageRating(sanPhamId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    public long countReviews(Integer sanPhamId) {
        return danhGiaRepository.countBySanPhamId_sanpham(sanPhamId);
    }

    public boolean hasUserReviewed(NguoiDung user, SanPham sanPham) {
        return danhGiaRepository.existsByNguoiDungAndSanPham(user, sanPham);
    }
}
