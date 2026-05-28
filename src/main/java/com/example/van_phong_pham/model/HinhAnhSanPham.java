package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "HinhAnhSanPham")
@Data
public class HinhAnhSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_hinhanh;

    @Column(nullable = false)
    private String duong_dan; // file path on server

    @Column(columnDefinition = "BIT DEFAULT 0")
    private Boolean la_anh_chinh = false; // is primary image

    @ManyToOne
    @JoinColumn(name = "id_sanpham", nullable = false)
    private SanPham sanPham;
}
