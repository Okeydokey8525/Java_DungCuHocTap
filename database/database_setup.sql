-- 1. Tạo cơ sở dữ liệu
CREATE DATABASE QuanLyVanPhongPham;

-- 2. Chọn cơ sở dữ liệu này để làm việc
USE QuanLyVanPhongPham;

-- 1. Bảng Vai Trò (Tạo trước để gán cho Người dùng)
CREATE TABLE VaiTro (
    id_vaitro INT PRIMARY KEY AUTO_INCREMENT,
    ten_vaitro VARCHAR(50) NOT NULL
);

-- 2. Bảng Danh Mục (Tạo trước để gán cho Sản phẩm)
CREATE TABLE DanhMuc (
    id_danhmuc INT PRIMARY KEY AUTO_INCREMENT,
    ten_danhmuc VARCHAR(100) NOT NULL
);

-- 3. Bảng Người Dùng
CREATE TABLE NguoiDung (
    id_nguoidung INT PRIMARY KEY AUTO_INCREMENT,
    ho_ten VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    mat_khau VARCHAR(255) NOT NULL,
    so_dien_thoai VARCHAR(15),
    id_vaitro INT,
    FOREIGN KEY (id_vaitro) REFERENCES VaiTro(id_vaitro)
);

-- 4. Bảng Sản Phẩm
CREATE TABLE SanPham (
    id_sanpham INT PRIMARY KEY AUTO_INCREMENT,
    ten_sanpham VARCHAR(200) NOT NULL,
    gia DOUBLE NOT NULL,
    hinh_anh VARCHAR(255),
    mo_ta TEXT,
    id_danhmuc INT,
    FOREIGN KEY (id_danhmuc) REFERENCES DanhMuc(id_danhmuc)
);

-- 5. Bảng Đơn Hàng
CREATE TABLE DonHang (
    id_donhang INT PRIMARY KEY AUTO_INCREMENT,
    ngay_dat DATETIME DEFAULT CURRENT_TIMESTAMP,
    tong_tien DOUBLE,
    trang_thai VARCHAR(50), -- Chờ duyệt, Đang giao, Đã giao...
    id_nguoidung INT,
    FOREIGN KEY (id_nguoidung) REFERENCES NguoiDung(id_nguoidung)
);

-- 6. Bảng Chi Tiết Đơn Hàng
CREATE TABLE ChiTietDonHang (
    id_chitiet INT PRIMARY KEY AUTO_INCREMENT,
    so_luong INT NOT NULL,
    gia_ban DOUBLE NOT NULL,
    id_donhang INT,
    id_sanpham INT,
    FOREIGN KEY (id_donhang) REFERENCES DonHang(id_donhang),
    FOREIGN KEY (id_sanpham) REFERENCES SanPham(id_sanpham)
);

-- Du lieu mau
-- 1. Thêm Vai trò
INSERT INTO VaiTro (ten_vaitro) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

-- 2. Thêm Danh mục mẫu
INSERT INTO DanhMuc (ten_danhmuc) VALUES ('Bút viết'), ('Tập vở'), ('Dụng cụ học tập');

-- 3. Thêm Người dùng mẫu (Mật khẩu: 123456)
INSERT INTO NguoiDung (ho_ten, email, mat_khau, so_dien_thoai, id_vaitro)
VALUES
    ('Admin HUIT', 'admin@huit.edu.vn', '$2a$10$R9h/lIPzHZB5J1Gf.83LDe9XU7nS6WdYvX0vX0vX0vX0vX0vX0vX0', '0123456789', 1),
    ('Nguyễn Văn A', 'user@gmail.com', '$2a$10$R9h/lIPzHZB5J1Gf.83LDe9XU7nS6WdYvX0vX0vX0vX0vX0vX0vX0', '0987654321', 2),
    ('Trần Thị B', 'user2@gmail.com', '$2a$10$R9h/lIPzHZB5J1Gf.83LDe9XU7nS6WdYvX0vX0vX0vX0vX0vX0vX0', '0333444555', 2);

-- 4. Thêm Sản phẩm mẫu (Gộp lại thành 1 câu lệnh để tránh lỗi)
INSERT INTO SanPham (ten_sanpham, gia, hinh_anh, mo_ta, id_danhmuc)
VALUES
    ('Bút bi Thiên Long TL-027', 5000, 'but_bi.jpg', 'Bút bi quốc dân cho sinh viên', 1), -- ID 1
    ('Vở Deli 120 trang', 12000, 'vo_deli.jpg', 'Giấy trắng, kẻ ngang chuẩn', 2),        -- ID 2
    ('Bút Gel Deli A067', 8000, 'but_gel_deli.jpg', 'Bút viết trơn, mực đậm', 1),       -- ID 3
    ('Thước kẻ HUIT 20cm', 4000, 'thuoc_huit.jpg', 'Thước nhựa dẻo in logo trường', 3), -- ID 4
    ('Combo 10 vở 200 trang', 120000, 'combo_vo.jpg', 'Giấy định lượng cao', 2),         -- ID 5
    ('Máy tính Casio fx-580VNX', 650000, 'casio_580.jpg', 'Máy tính chuyên dụng', 3),    -- ID 6
    ('Bút xóa kéo Thiên Long', 15000, 'xoa_keo.jpg', 'Dễ dàng sử dụng', 3),             -- ID 7
    ('Balo sinh viên HUIT', 250000, 'balo_huit.jpg', 'Có ngăn đựng laptop', 3),         -- ID 8
    ('Bút chì 2B Thiên Long', 4000, 'but_chi_2b.jpg', 'Thân gỗ bền', 1),                -- ID 9
    ('Gôm tẩy Deli màu đen', 5000, 'gom_deli.jpg', 'Tẩy sạch, ít bụi vụn', 3),          -- ID 10
    ('Giấy A4 Double A 70gsm', 85000, 'giay_a4.jpg', 'Ram 500 tờ', 2),                  -- ID 11
    ('Sổ tay HUIT Limited', 45000, 'so_tay_huit.jpg', 'Bìa cứng logo trường', 2);      -- ID 12

-- 5. Thêm Đơn hàng mẫu
-- Đơn 1: Đã giao
INSERT INTO DonHang (tong_tien, trang_thai, id_nguoidung) VALUES (17000, 'Đã giao', 2);
INSERT INTO ChiTietDonHang (so_luong, gia_ban, id_donhang, id_sanpham) VALUES (1, 5000, 1, 1), (1, 12000, 1, 2);

-- Đơn 2: Chờ duyệt
INSERT INTO DonHang (tong_tien, trang_thai, id_nguoidung) VALUES (258000, 'Chờ duyệt', 3);
INSERT INTO ChiTietDonHang (so_luong, gia_ban, id_donhang, id_sanpham) VALUES (1, 250000, 2, 8), (2, 4000, 2, 9);

-- Đơn 3: Đã hủy
INSERT INTO DonHang (tong_tien, trang_thai, id_nguoidung) VALUES (650000, 'Đã hủy', 2);
INSERT INTO ChiTietDonHang (so_luong, gia_ban, id_donhang, id_sanpham) VALUES (1, 650000, 3, 6);


