# AGENTS.md

## 1) Project Overview

**Tên dự án:** Java_DungCuHocTap  
**Loại dự án:** Spring Boot e-commerce (cửa hàng dụng cụ học tập)  
**Mục tiêu:** Xây dựng website bán dụng cụ học tập cho sinh viên/học sinh, hỗ trợ xem sản phẩm, đặt hàng, thanh toán, quản lý hồ sơ và lịch sử mua hàng.

### Kiến trúc hiện tại (giữ nguyên)
- **Pattern:** MVC (Model - View - Controller)
- **Controller hiện có:** `AuthController`, `UserController`, `CheckoutController`, `LoginController`
- **Entity hiện có:** `NguoiDung`, `DonHang`, `ChiTietDonHang`, `SanPham`
- **View:** Thymeleaf templates trong `src/main/resources/templates`
- **Layout dùng chung:** `layout.html`

> Lưu ý: Mọi thay đổi mới phải đi theo đúng kiến trúc hiện tại, không làm vỡ cấu trúc project đang có.

---

## 2) Tech Stack

- **Ngôn ngữ:** Java
- **Framework:** Spring Boot
- **Template engine:** Thymeleaf (`th:*`)
- **Build tool:** Maven (`pom.xml`, `mvnw`)
- **Config:** `src/main/resources/application.properties`
- **Frontend:** HTML/CSS/JS cơ bản (UI đơn giản, sạch, phù hợp project sinh viên)

### Ràng buộc công nghệ
- ✅ Dùng đúng Spring Boot + Thymeleaf hiện tại.
- ✅ Tái sử dụng layout chung (`layout.html`) cho trang mới.
- ❌ Không thêm framework mới (VD: React, Vue, Angular, framework CSS nặng) nếu chưa có yêu cầu chính thức.

---

## 3) Rules (Coding + An toàn code cũ + Clean Code)

## 3.1 Quy tắc bắt buộc
1. **Follow MVC pattern**
   - Controller xử lý request/response.
   - Service/logic nghiệp vụ không nhồi vào template.
   - Template chỉ hiển thị dữ liệu bằng Thymeleaf.

2. **Dùng Thymeleaf đúng chuẩn**
   - Ưu tiên `th:text`, `th:if`, `th:each`, `th:href`, `th:action`, ...
   - Không hardcode URL khi có thể dùng `@{...}`.

3. **Reuse layout.html**
   - Trang mới cần bám layout chung để đồng nhất giao diện.

4. **Không phá code cũ / không phá structure**
   - Không đổi tên class/controller/entity/route hiện có nếu chưa đánh giá ảnh hưởng.
   - Không tự ý thay đổi luồng checkout/login/user hiện tại.
   - Khi refactor, hành vi đầu ra phải giữ nguyên.

5. **Không introduce framework mới**
   - Chỉ dùng những công nghệ đã có trong project.

## 3.2 Clean code guidelines
- Tên biến/hàm/class rõ nghĩa, thống nhất ngôn ngữ đặt tên theo codebase hiện tại.
- Method ngắn, tránh lồng điều kiện sâu.
- Không lặp code không cần thiết; tách hàm khi logic dài.
- Comment cho logic khó, tránh comment dư thừa.
- Chỉ chỉnh sửa file liên quan trực tiếp đến task.

## 3.3 Quy tắc khi sửa UI
- Giao diện đơn giản, rõ ràng, dễ dùng cho bối cảnh đồ án sinh viên.
- Giữ nhất quán font, spacing, màu sắc với layout hiện tại.
- Không thêm hiệu ứng phức tạp hoặc phụ thuộc frontend nặng.

> Ví dụ đúng: Thêm trang danh sách đơn hàng bằng Thymeleaf `th:each`, kế thừa bố cục `layout.html`, giữ route/controller theo chuẩn MVC.

---

## 4) Cách chạy project

### 4.1 Cài dependencies & build cơ bản
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

### 4.4 Build package
```bash
./mvnw clean package
```

---

## 5) Agent Behavior (AI nên hành xử)

AI agent khi làm việc trong repo này phải:

1. **Hiểu yêu cầu & scope trước khi code**
   - Nếu task mơ hồ, ưu tiên suy luận theo cấu trúc hiện tại thay vì tự ý thiết kế lại.

2. **Tôn trọng structure sẵn có**
   - Bám theo MVC, controller/entity/template đang có.
   - Không di chuyển hoặc đổi tên thành phần lõi nếu không được yêu cầu.

3. **Ưu tiên thay đổi nhỏ, an toàn**
   - Sửa đúng điểm cần thiết, không “đập đi làm lại”.

4. **Minh bạch thay đổi**
   - Nêu rõ file nào đã sửa và lý do.
   - Nêu lệnh đã chạy để kiểm tra (`./mvnw test` hoặc giải thích vì sao chưa chạy được).

5. **Đảm bảo chuẩn Thymeleaf + layout tái sử dụng**
   - Trang mới phải dùng `th:*` và tích hợp với `layout.html`.

6. **Giữ giao tiếp rõ ràng**
   - Trả lời dạng Markdown, ngắn gọn, có checklist/bullet khi cần.

> Ví dụ hành xử tốt: “Tôi thêm logic ở `CheckoutController`, cập nhật template theo `th:*`, tái sử dụng `layout.html`, không đổi route cũ, và đã chạy `./mvnw test` thành công.”
