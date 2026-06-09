# 📚 Đồ án Java: Website Bán Dụng Cụ Học Tập HUIT
> Dự án được phát triển bởi Nhóm - Khoa CNTT HUIT.

## 👥 Thành viên nhóm & Phân công
1. **Lê Đức Lương (Leader):** Cấu hình hệ thống, Bảo mật, CSDL, Trang cá nhân & Thanh toán.
2. **Trần Nguyễn Phú Hoàng:** Thiết kế UI/UX, Trang chủ, Danh mục & Chi tiết sản phẩm.
3. **Tiến Bùi:** Quản trị Admin, Logic Giỏ hàng & Duyệt đơn hàng.

## 🛠 Công nghệ sử dụng
- **Ngôn ngữ:** Java 21
- **Framework:** Spring Boot, Spring Security, JPA, Thymeleaf
- **CSDL:** SQL Server
- **Giao diện:** Tailwind CSS, Lucide Icons

## 🚀 Các tính năng Thương mại (Mới)
- **Thanh toán trực tuyến:** Tích hợp cổng thanh toán VNPay.
- **Xác thực:** Đăng nhập thông thường và Đăng nhập mạng xã hội (Google OAuth2).
- **Giao tiếp:** Gửi email tự động (Chào mừng, Khôi phục mật khẩu, Xác nhận đơn hàng).
- **Vận chuyển:** Theo dõi đơn vị vận chuyển và Mã vận đơn.
- **Đánh giá:** Chỉ cho phép khách hàng đã mua và nhận hàng thành công được đánh giá sản phẩm.
- **Khuyến mãi:** Hỗ trợ mã giảm giá theo số tiền cố định và phần trăm (kèm giới hạn mức giảm tối đa).
- **SEO & Security:** Tối ưu thẻ meta Open Graph và chống Clickjacking.

## ⚙️ Hướng dẫn cài đặt bằng VS Code
1. **Cài đặt tiện ích mở rộng (Extensions):**
   - Mở VS Code, cài đặt `Extension Pack for Java` (của Microsoft).
   - Cài đặt `Spring Boot Extension Pack` (của VMware).
2. **Clone dự án:** `git clone https://github.com/Okeydokey8525/Java_DungCuHocTap.git`
3. **Cài đặt Cơ Sở Dữ Liệu:**
   - Sử dụng SQL Server.
   - Chạy script SQL (nếu có trong mục `database/`) hoặc để Hibernate tự động `update` bảng (nhờ cấu hình `spring.jpa.hibernate.ddl-auto=update`).
   - Mở file `src/main/resources/application.properties` và sửa các thông tin `spring.datasource.password` và `spring.datasource.url` cho khớp với SQL Server của bạn.
4. **Cấu hình biến môi trường (Nếu cần gửi email/VNPay/Google Login thực tế):**
   - Cập nhật tài khoản email (app password) trong `application.properties`.
   - Cập nhật `vnpay.tmnCode` và `vnpay.hashSecret`.
   - Cập nhật `Google Client ID` và `Secret`.
5. **Chạy ứng dụng:**
   - Trong VS Code, mở tab **Spring Boot Dashboard** hoặc file `VanPhongPhamApplication.java`.
   - Bấm **Run** hoặc **Debug**.
   - Truy cập vào: `http://localhost:8080`.

## 🔑 Tài khoản Test (Mật khẩu chung: 123456)
- **Admin:** `admin@huit.edu.vn`
- **User:** `user@gmail.com`
