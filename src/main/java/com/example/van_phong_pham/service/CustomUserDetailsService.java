package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        NguoiDung user = nguoiDungRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email);
        }

        boolean enabled = user.getTrang_thai() != null ? user.getTrang_thai() : true;

        return User.withUsername(user.getEmail())
                .password(user.getMat_khau())
                .disabled(!enabled)
                .authorities(user.getVaiTro() != null ? user.getVaiTro().getTen_vaitro() : "ROLE_USER")
                .build();
    }
}