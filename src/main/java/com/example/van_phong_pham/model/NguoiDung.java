package com.example.van_phong_pham.model;


import jakarta.persistence.*;
import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "id_vaitro") // Liên kết với bảng VaiTro
    private VaiTro vaiTro;
}
