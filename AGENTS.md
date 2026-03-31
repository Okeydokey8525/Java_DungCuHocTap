# AGENTS.md

## 1) Project Overview

**Tên dự án:** Java_DungCuHocTap  
**Mục tiêu:** Xây dựng hệ thống web bán dụng cụ học tập, hỗ trợ người dùng xem sản phẩm, mua hàng và theo dõi lịch sử đơn hàng.

### Chức năng chính
- Hiển thị danh sách sản phẩm và chi tiết sản phẩm.
- Quản lý giỏ hàng và quy trình thanh toán.
- Quản lý hồ sơ người dùng.
- Theo dõi lịch sử mua hàng.

> Ví dụ: Người dùng vào trang chủ, chọn sản phẩm, thêm vào giỏ, thanh toán và xem lại đơn trong mục lịch sử mua hàng.

---

## 2) Tech Stack

- **Ngôn ngữ:** Java
- **Framework backend:** Spring Boot
- **Build tool:** Maven (`pom.xml`, `mvnw`)
- **Template engine:** Thymeleaf (các file HTML trong `src/main/resources/templates`)
- **Cấu hình:** `application.properties`

---

## 3) Coding Rules

## 3.1 Quy tắc chung
- Ưu tiên **đọc hiểu code cũ trước khi sửa**.
- Không thay đổi hành vi hệ thống nếu không có yêu cầu rõ ràng.
- Giữ thay đổi ở phạm vi nhỏ, đúng mục tiêu.
- Luôn viết code theo hướng dễ đọc, dễ bảo trì.

## 3.2 Không phá code cũ
- Không xóa hoặc đổi tên API, method, class, route đang được sử dụng nếu chưa đánh giá ảnh hưởng.
- Khi refactor, đảm bảo kết quả đầu ra không đổi.
- Nếu cần đổi logic, phải nêu rõ lý do và ảnh hưởng trong commit/PR.

## 3.3 Clean code
- Đặt tên biến/hàm/class rõ nghĩa, nhất quán.
- Method ngắn gọn, tránh lồng điều kiện quá sâu.
- Tách logic nghiệp vụ khỏi xử lý giao diện.
- Tránh lặp code (DRY), nhưng không lạm dụng abstraction.
- Thêm comment cho đoạn logic phức tạp; không comment những gì đã hiển nhiên.

## 3.4 Quy tắc khi chỉnh sửa file
- Chỉ sửa file liên quan trực tiếp đến yêu cầu.
- Không tự ý format toàn bộ project nếu không cần thiết.
- Không đưa thư viện mới vào dự án nếu chưa có lý do rõ ràng.

---

## 4) Cách chạy project

### 4.1 Cài đặt dependencies
Dùng Maven Wrapper để đảm bảo đúng phiên bản Maven:

```bash
./mvnw clean install
```

### 4.2 Chạy ứng dụng

```bash
./mvnw spring-boot:run
```

### 4.3 Chạy test

```bash
./mvnw test
```

### 4.4 Build artifact (tùy chọn)

```bash
./mvnw clean package
```

---

## 5) Agent Behavior (AI nên hành xử như thế nào)

AI agent làm việc trong project này cần tuân thủ:

1. **Hiểu yêu cầu trước khi sửa**
   - Tóm tắt lại mục tiêu ngắn gọn trước khi bắt đầu nếu yêu cầu phức tạp.

2. **Ưu tiên an toàn cho codebase**
   - Không thay đổi lớn ngoài phạm vi.
   - Không phá vỡ luồng hiện có.

3. **Minh bạch thay đổi**
   - Liệt kê file đã sửa và lý do.
   - Nêu rõ command đã chạy để kiểm tra.

4. **Kiểm chứng sau khi sửa**
   - Ít nhất chạy `./mvnw test` nếu có thể.
   - Nếu không chạy được, phải nói rõ lý do.

5. **Giữ chuẩn clean code**
   - Code rõ ràng, có cấu trúc.
   - Không thêm phần “thử nghiệm tạm” vào production code.

6. **Giao tiếp ngắn gọn, rõ ràng**
   - Trả lời bằng Markdown, có tiêu đề và bullet points khi cần.
   - Nêu rủi ro/giả định nếu có.

> Ví dụ hành xử tốt: “Tôi chỉ chỉnh sửa 2 file liên quan đến luồng thanh toán, giữ nguyên API cũ, và đã chạy `./mvnw test` thành công.”

