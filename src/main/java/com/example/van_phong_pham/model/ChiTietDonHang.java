package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ChiTietDonHang")
@Data
public class ChiTietDonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_chitiet;

    @ManyToOne
    @JoinColumn(name = "id_donhang", nullable = false)
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "id_sanpham", nullable = false)
    private SanPham sanPham;

    @Column(nullable = false)
    private Integer so_luong;

    @Column(name = "don_gia", nullable = false)
    private Double don_gia; // Giá tại thời điểm mua

    @Column(name = "thanh_tien", nullable = false)
    private Double thanh_tien; // Thành tiền = don_gia * so_luong

    // Constructor mặc định
    public ChiTietDonHang() {
        this.so_luong = 1;
        this.don_gia = 0.0;
        this.thanh_tien = 0.0;
    }

    // Constructor với tham số
    public ChiTietDonHang(DonHang donHang, SanPham sanPham, Integer so_luong, Double don_gia) {
        this.donHang = donHang;
        this.sanPham = sanPham;
        this.so_luong = so_luong != null && so_luong > 0 ? so_luong : 1;
        this.don_gia = don_gia != null ? don_gia : 0.0;
        this.thanh_tien = this.don_gia * this.so_luong;
    }

    // Cập nhật thành tiền khi thay đổi số lượng hoặc đơn giá
    @PrePersist
    @PreUpdate
    public void calculateThanhTien() {
        if (don_gia != null && so_luong != null) {
            this.thanh_tien = don_gia * so_luong;
        }
    }
}
