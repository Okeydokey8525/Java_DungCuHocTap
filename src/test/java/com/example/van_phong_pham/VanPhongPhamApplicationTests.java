package com.example.van_phong_pham;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class VanPhongPhamApplicationTests {

	@Test
	void contextLoads() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String[] hashes = {
			"$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy", // admin@huit.edu.vn
			"$2a$10$VBO2BGHbpPberx/yxZ4L7O2f3NfenXRyHVQntzNm2f5QYrGdYupZC", // leducluong.080525@gmail.com
			"$2a$10$ogG7dw62SWbIGgq7..fkSuzNMLZHrP3Z0jrArA9aeXGJTc2cyuWA2", // leducluong.0805205@gmail.com
			"$2a$10$xALeHJlBAqYSqUb/kao7wOQLKEt0u0kKXthBhe66tGmS3yk4N7bsW", // leducluong.08052005@gmail.com
			"$2a$10$F2zHItW7gaFiEd8cCyNn2ej4ki8j9vdilRj/XGJR3cqOAF8cax9ye"  // luong@gmail.com
		};
		String[] passwordsToTest = {"123456", "12345678", "123", "luong123", "luong", "Luong123", "Luong", "123456789"};
		for (String hash : hashes) {
			System.out.println("Testing hash: " + hash);
			for (String pwd : passwordsToTest) {
				if (encoder.matches(pwd, hash)) {
					System.out.println("  MATCH FOUND: password is '" + pwd + "'");
				}
			}
		}
	}

}
