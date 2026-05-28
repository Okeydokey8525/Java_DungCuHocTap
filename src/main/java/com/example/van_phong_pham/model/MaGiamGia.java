package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "MaGiamGia")
@Data
public class MaGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_magiamgia;

    @Column(unique = true, nullable = false, length = 50)
    private String ma_code; // e.g. "SALE10", "FREESHIP"

    @Column(nullable = false, length = 20)
    private String loai_giam; // "PERCENT" or "FIXED"

    @Column(nullable = false)
    private Double gia_tri_giam; // 10 for 10%, or 50000 for 50k off

    private Double don_toi_thieu; // minimum order amount to use

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    private Integer so_luong; // total available
    private Integer da_su_dung = 0; // already used count

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean trang_thai = true; // active/inactive

    public MaGiamGia() {
        this.da_su_dung = 0;
        this.trang_thai = true;
    }

    // Check if coupon is valid
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        if (!trang_thai) return false;
        if (ngayBatDau != null && now.isBefore(ngayBatDau)) return false;
        if (ngayKetThuc != null && now.isAfter(ngayKetThuc)) return false;
        if (so_luong != null && da_su_dung >= so_luong) return false;
        return true;
    }

    // Calculate discount amount
    public Double calculateDiscount(Double orderTotal) {
        if (!isValid()) return 0.0;
        if (don_toi_thieu != null && orderTotal < don_toi_thieu) return 0.0;
        if ("PERCENT".equals(loai_giam)) {
            return orderTotal * gia_tri_giam / 100;
        } else {
            return Math.min(gia_tri_giam, orderTotal);
        }
    }
}
