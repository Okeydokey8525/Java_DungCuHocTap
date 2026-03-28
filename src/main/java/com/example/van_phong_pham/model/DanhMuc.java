package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DanhMuc")
@Data
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_danhmuc;

    private String ten_danhmuc;
}
