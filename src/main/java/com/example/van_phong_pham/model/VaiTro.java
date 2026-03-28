package com.example.van_phong_pham.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "VaiTro")
@Data
public class VaiTro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_vaitro;

    private String ten_vaitro;
}