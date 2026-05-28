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

    public VaiTro findByName(String name) {
        return vaiTroRepository.findByTen_vaitro(name);
    }

    public VaiTro findById(Integer id) {
        return vaiTroRepository.findById(id).orElse(null);
    }

    public VaiTro save(VaiTro vaiTro) {
        return vaiTroRepository.save(vaiTro);
    }
}