package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "NguoiDung")
@Data
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_nguoidung;

    private String ho_ten;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String mat_khau;

    private String so_dien_thoai;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String dia_chi;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean trang_thai = true;

    @Column(name = "ngay_tao", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayTao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "id_vaitro") // Liên kết với bảng VaiTro
    private VaiTro vaiTro;
}
