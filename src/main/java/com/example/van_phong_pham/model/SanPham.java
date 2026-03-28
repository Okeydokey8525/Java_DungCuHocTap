package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "SanPham")
@Data
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_sanpham;

    private String ten_sanpham;
    private Double gia;
    private String hinh_anh;

    @Column(columnDefinition = "TEXT")
    private String mo_ta;

    @ManyToOne
    @JoinColumn(name = "id_danhmuc") // Khóa ngoại kết nối với bảng DanhMuc
    private DanhMuc danhMuc;
}