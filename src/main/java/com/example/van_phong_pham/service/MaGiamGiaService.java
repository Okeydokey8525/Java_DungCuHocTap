package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.MaGiamGia;
import com.example.van_phong_pham.repository.MaGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaGiamGiaService {
    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    public List<MaGiamGia> getAllCoupons() {
        return maGiamGiaRepository.findAll();
    }

    public List<MaGiamGia> getActiveCoupons() {
        return maGiamGiaRepository.findByTrang_thaiTrue();
    }

    public MaGiamGia findById(Integer id) {
        return maGiamGiaRepository.findById(id).orElse(null);
    }

    public MaGiamGia findByCode(String code) {
        Optional<MaGiamGia> opt = maGiamGiaRepository.findByMa_code(code);
        return opt.orElse(null);
    }

    public void saveCoupon(MaGiamGia coupon) {
        maGiamGiaRepository.save(coupon);
    }

    public void deleteCoupon(Integer id) {
        maGiamGiaRepository.deleteById(id);
    }

    // Validate and calculate discount
    public Double validateAndApply(String code, Double orderTotal) {
        MaGiamGia coupon = findByCode(code);
        if (coupon == null) return 0.0;
        return coupon.calculateDiscount(orderTotal);
    }

    // Mark coupon as used
    public void incrementUsage(String code) {
        MaGiamGia coupon = findByCode(code);
        if (coupon != null) {
            coupon.setDa_su_dung(coupon.getDa_su_dung() + 1);
            maGiamGiaRepository.save(coupon);
        }
    }
}
