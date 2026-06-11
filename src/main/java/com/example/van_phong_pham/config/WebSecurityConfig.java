package com.example.van_phong_pham.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.example.van_phong_pham.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final org.springframework.context.ApplicationContext applicationContext;

    public WebSecurityConfig(org.springframework.context.ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/san-pham/**", "/danh-muc/**",
                                "/css/**", "/js/**", "/JS/**", "/images/**", "/uploads/**",
                                "/login", "/register", "/quen-mat-khau",
                                "/gioi-thieu", "/lien-he").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/profile/**", "/orders/**", "/checkout/**", "/cart/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(applicationContext.getBean(CustomOAuth2UserService.class))
                        )
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                )
                .rememberMe(remember -> remember
                        .key("huit-stationery-remember-me-key")
                        .tokenValiditySeconds(7 * 24 * 60 * 60) // 7 days
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:uploads/", "file:uploads/images/");
            }
        };
    }
}
