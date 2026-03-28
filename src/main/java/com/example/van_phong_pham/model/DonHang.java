package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "DonHang")
@Data
public class DonHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_donhang;

    @Column(name = "ngay_dat", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ngayDat;

    private Double tong_tien;

    private String trang_thai; // Chờ duyệt, Đang giao, Đã giao...

    @ManyToOne
    @JoinColumn(name = "id_nguoidung") // Ai là người đặt đơn này?
    private NguoiDung nguoiDung;
}
