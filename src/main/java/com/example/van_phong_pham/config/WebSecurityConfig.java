package com.example.van_phong_pham.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // Trong WebSecurityConfig.java
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/san-pham/**", "/css/**", "/js/**", "/images/**", "/login", "/register").permitAll()
                        .requestMatchers("/profile/**", "/orders/**", "/checkout/**").authenticated() // Thêm dòng này
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login") // Tên URL mình vừa tạo ở Controller
                        .loginProcessingUrl("/login") // URL để xử lý dữ liệu Form gửi lên
                        .defaultSuccessUrl("/home", true) // Thành công thì đi đâu
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }
}
