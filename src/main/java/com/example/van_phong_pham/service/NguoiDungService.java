package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Tìm người dùng theo email (Dùng cho logic Đăng nhập)
    public NguoiDung findByEmail(String email) {
        return nguoiDungRepository.findByEmail(email);
    }

    // Lưu người dùng mới (Có mã hóa mật khẩu)
    public void saveUser(NguoiDung user) {
        user.setMat_khau(passwordEncoder.encode(user.getMat_khau()));
        nguoiDungRepository.save(user);
    }
}
