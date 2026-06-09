package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.VaiTro;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final NguoiDungService nguoiDungService;
    private final VaiTroService vaiTroService;
    private final PasswordEncoder passwordEncoder; // You can mock one or inject properly

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        NguoiDung user = nguoiDungService.findByEmail(email);
        if (user == null) {
            user = new NguoiDung();
            user.setEmail(email);
            user.setHo_ten(name);
            user.setMat_khau(passwordEncoder.encode(UUID.randomUUID().toString())); // Random password
            user.setTrang_thai(true);

            VaiTro userRole = vaiTroService.findByName("ROLE_USER");
            if (userRole == null) {
                userRole = new VaiTro();
                userRole.setTen_vaitro("ROLE_USER");
                userRole = vaiTroService.save(userRole);
            }
            user.setVaiTro(userRole);

            nguoiDungService.saveUser(user);
        }

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                "email" // the key attribute name
        );
    }
}
