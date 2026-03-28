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

    private Integer so_luong;

    private Double gia_ban; // Lưu giá tại thời điểm mua

    @ManyToOne
    @JoinColumn(name = "id_donhang")
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "id_sanpham")
    private SanPham sanPham;
}
