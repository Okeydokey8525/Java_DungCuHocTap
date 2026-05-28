package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "SanPham")
@Data
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_sanpham;

    private String ten_sanpham;
    private Double gia;
    private Double gia_giam;
    private String hinh_anh;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String mo_ta;

    private Integer so_luong = 0;
    private String thuong_hieu;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean trang_thai = true;

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_danhmuc") // Khóa ngoại kết nối với bảng DanhMuc
    private DanhMuc danhMuc;
}