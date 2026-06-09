package com.example.van_phong_pham;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class VanPhongPhamApplication {

	public static void main(String[] args) {
		SpringApplication.run(VanPhongPhamApplication.class, args);
	}

}
//2. Cấu hình Bảo mật & Đăng nhập