package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "DanhGia")
@Data
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_danhgia;

    @Column(columnDefinition = "NVARCHAR(1000)")
    private String noi_dung;

    @Column(nullable = false)
    private Integer so_sao; // 1-5

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean trang_thai = true; // true = approved, false = hidden

    @ManyToOne
    @JoinColumn(name = "id_nguoidung", nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "id_sanpham", nullable = false)
    private SanPham sanPham;

    public DanhGia() {
        this.ngayTao = LocalDateTime.now();
        this.trang_thai = true;
    }
}
