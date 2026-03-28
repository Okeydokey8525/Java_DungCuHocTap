package com.example.van_phong_pham.service;


import com.example.van_phong_pham.model.VaiTro;
import com.example.van_phong_pham.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VaiTroService {
    @Autowired
    private VaiTroRepository vaiTroRepository;

    public List<VaiTro> getAllRoles() {
        return vaiTroRepository.findAll();
    }
}