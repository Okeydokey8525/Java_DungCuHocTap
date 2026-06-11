package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // Tìm người dùng theo email (Dùng cho logic Đăng nhập)
    public NguoiDung findByEmail(String email) {
        return nguoiDungRepository.findByEmail(email);
    }

    // Lưu người dùng mới (Có mã hóa mật khẩu)
    public void saveUser(NguoiDung user) {
        user.setMat_khau(passwordEncoder.encode(user.getMat_khau()));
        nguoiDungRepository.save(user);
    }

    public List<NguoiDung> getAllUsers() {
        return nguoiDungRepository.findAll();
    }

    public NguoiDung findById(Integer id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    public void updateUser(NguoiDung user) {
        nguoiDungRepository.save(user);
    }

    public void toggleUserStatus(Integer userId) {
        NguoiDung user = findById(userId);
        if (user != null) {
            user.setTrang_thai(user.getTrang_thai() != null ? !user.getTrang_thai() : false);
            nguoiDungRepository.save(user);
        }
    }

    public long countUsers() {
        return nguoiDungRepository.count();
    }

    public void changePassword(NguoiDung user, String newPassword) {
        user.setMat_khau(passwordEncoder.encode(newPassword));
        nguoiDungRepository.save(user);
    }
}
