package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "GioHang")
@Data
public class GioHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_giohang;

    @ManyToOne
    @JoinColumn(name = "id_nguoidung", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "id_sanpham", nullable = false)
    private SanPham sanPham;

    @Column(nullable = false)
    private Integer so_luong;

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_capnhat", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime ngayCapNhat;

    // Constructor mặc định
    public GioHang() {
        this.so_luong = 1;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Constructor với tham số
    public GioHang(NguoiDung nguoiDung, SanPham sanPham, Integer so_luong) {
        this.nguoiDung = nguoiDung;
        this.sanPham = sanPham;
        this.so_luong = so_luong != null && so_luong > 0 ? so_luong : 1;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Tính tổng tiền cho item này
    public Double getTongTien() {
        return sanPham != null ? sanPham.getGia() * so_luong : 0.0;
    }
}
