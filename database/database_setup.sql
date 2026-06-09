-- 1. Create Database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'HUIT_Stationery_java')
BEGIN
    CREATE DATABASE HUIT_Stationery_java;
END
GO

USE HUIT_Stationery_java;
GO

-- Drop existing tables (in reverse FK order)
IF OBJECT_ID('ChiTietDonHang', 'U') IS NOT NULL DROP TABLE ChiTietDonHang;
IF OBJECT_ID('DonHang', 'U') IS NOT NULL DROP TABLE DonHang;
IF OBJECT_ID('GioHang', 'U') IS NOT NULL DROP TABLE GioHang;
IF OBJECT_ID('DanhGia', 'U') IS NOT NULL DROP TABLE DanhGia;
IF OBJECT_ID('HinhAnhSanPham', 'U') IS NOT NULL DROP TABLE HinhAnhSanPham;
IF OBJECT_ID('MaGiamGia', 'U') IS NOT NULL DROP TABLE MaGiamGia;
IF OBJECT_ID('SanPham', 'U') IS NOT NULL DROP TABLE SanPham;
IF OBJECT_ID('DanhMuc', 'U') IS NOT NULL DROP TABLE DanhMuc;
IF OBJECT_ID('NguoiDung', 'U') IS NOT NULL DROP TABLE NguoiDung;
IF OBJECT_ID('VaiTro', 'U') IS NOT NULL DROP TABLE VaiTro;
GO

-- 1. VaiTro (Role)
CREATE TABLE VaiTro (
    id_vaitro INT IDENTITY(1,1) PRIMARY KEY,
    ten_vaitro NVARCHAR(50) NOT NULL
);

-- 2. DanhMuc (Category)
CREATE TABLE DanhMuc (
    id_danhmuc INT IDENTITY(1,1) PRIMARY KEY,
    ten_danhmuc NVARCHAR(100) NOT NULL
);

-- 3. NguoiDung (User)
CREATE TABLE NguoiDung (
    id_nguoidung INT IDENTITY(1,1) PRIMARY KEY,
    ho_ten NVARCHAR(100),
    email NVARCHAR(100) UNIQUE NOT NULL,
    mat_khau NVARCHAR(255) NOT NULL,
    so_dien_thoai NVARCHAR(15),
    dia_chi NVARCHAR(500),
    trang_thai BIT DEFAULT 1,
    ngay_tao DATETIME DEFAULT GETDATE(),
    id_vaitro INT,
    FOREIGN KEY (id_vaitro) REFERENCES VaiTro(id_vaitro)
);

-- 4. SanPham (Product)
CREATE TABLE SanPham (
    id_sanpham INT IDENTITY(1,1) PRIMARY KEY,
    ten_sanpham NVARCHAR(200) NOT NULL,
    gia FLOAT NOT NULL,
    gia_giam FLOAT NULL,
    hinh_anh NVARCHAR(255),
    mo_ta NVARCHAR(MAX),
    so_luong INT DEFAULT 0,
    thuong_hieu NVARCHAR(100),
    trang_thai BIT DEFAULT 1,
    ngay_tao DATETIME DEFAULT GETDATE(),
    id_danhmuc INT,
    FOREIGN KEY (id_danhmuc) REFERENCES DanhMuc(id_danhmuc)
);

-- 5. HinhAnhSanPham (Product Images)
CREATE TABLE HinhAnhSanPham (
    id_hinhanh INT IDENTITY(1,1) PRIMARY KEY,
    duong_dan NVARCHAR(255) NOT NULL,
    la_anh_chinh BIT DEFAULT 0,
    id_sanpham INT NOT NULL,
    FOREIGN KEY (id_sanpham) REFERENCES SanPham(id_sanpham)
);

-- 6. GioHang (Cart)
CREATE TABLE GioHang (
    id_giohang INT IDENTITY(1,1) PRIMARY KEY,
    so_luong INT NOT NULL DEFAULT 1,
    ngay_tao DATETIME DEFAULT GETDATE(),
    ngay_capnhat DATETIME DEFAULT GETDATE(),
    id_nguoidung INT NOT NULL,
    id_sanpham INT NOT NULL,
    FOREIGN KEY (id_nguoidung) REFERENCES NguoiDung(id_nguoidung),
    FOREIGN KEY (id_sanpham) REFERENCES SanPham(id_sanpham)
);

-- 7. DonHang (Order)
CREATE TABLE DonHang (
    id_donhang INT IDENTITY(1,1) PRIMARY KEY,
    ngay_dat DATETIME DEFAULT GETDATE(),
    tong_tien FLOAT,
    trang_thai NVARCHAR(50),
    dia_chi_giao_hang NVARCHAR(500),
    ghi_chu NVARCHAR(500),
    phuong_thuc_thanh_toan NVARCHAR(50),
    phi_van_chuyen FLOAT,
    id_nguoidung INT,
    FOREIGN KEY (id_nguoidung) REFERENCES NguoiDung(id_nguoidung)
);

-- 8. ChiTietDonHang (Order Detail)
CREATE TABLE ChiTietDonHang (
    id_chitiet INT IDENTITY(1,1) PRIMARY KEY,
    so_luong INT NOT NULL,
    don_gia FLOAT NOT NULL,
    thanh_tien FLOAT NOT NULL,
    id_donhang INT NOT NULL,
    id_sanpham INT NOT NULL,
    FOREIGN KEY (id_donhang) REFERENCES DonHang(id_donhang),
    FOREIGN KEY (id_sanpham) REFERENCES SanPham(id_sanpham)
);

-- 9. DanhGia (Review)
CREATE TABLE DanhGia (
    id_danhgia INT IDENTITY(1,1) PRIMARY KEY,
    noi_dung NVARCHAR(1000),
    so_sao INT NOT NULL,
    ngay_tao DATETIME DEFAULT GETDATE(),
    trang_thai BIT DEFAULT 1,
    id_nguoidung INT NOT NULL,
    id_sanpham INT NOT NULL,
    FOREIGN KEY (id_nguoidung) REFERENCES NguoiDung(id_nguoidung),
    FOREIGN KEY (id_sanpham) REFERENCES SanPham(id_sanpham)
);

-- 10. MaGiamGia (Coupon)
CREATE TABLE MaGiamGia (
    id_magiamgia INT IDENTITY(1,1) PRIMARY KEY,
    ma_code NVARCHAR(50) UNIQUE NOT NULL,
    loai_giam NVARCHAR(20) NOT NULL, -- 'PERCENT' or 'FIXED'
    gia_tri_giam FLOAT NOT NULL,
    don_toi_thieu FLOAT,
    ngay_bat_dau DATETIME,
    ngay_ket_thuc DATETIME,
    so_luong INT,
    da_su_dung INT DEFAULT 0,
    trang_thai BIT DEFAULT 1
);
GO

-- ==========================================
-- SAMPLE DATA
-- ==========================================

-- 1. Roles
INSERT INTO VaiTro (ten_vaitro) VALUES (N'ROLE_ADMIN'), (N'ROLE_USER');

-- 2. Categories
INSERT INTO DanhMuc (ten_danhmuc) VALUES 
    (N'Bút viết'), (N'Tập vở'), (N'Dụng cụ học tập'), (N'Văn phòng phẩm'), (N'Balo & Túi');

-- 3. Users (Password: 123456 hashed with BCrypt)
INSERT INTO NguoiDung (ho_ten, email, mat_khau, so_dien_thoai, dia_chi, trang_thai, id_vaitro)
VALUES
    (N'Admin HUIT', 'admin@huit.edu.vn', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '0123456789', N'12 Nguyễn Văn Bảo, Q. Gò Vấp, TP.HCM', 1, 1),
    (N'Nguyễn Văn A', 'user@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '0987654321', N'45 Lê Lợi, Q.1, TP.HCM', 1, 2),
    (N'Trần Thị B', 'user2@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '0333444555', N'78 Trần Hưng Đạo, Q.5, TP.HCM', 1, 2);

-- 4. Products (with stock, brand, sale price)
INSERT INTO SanPham (ten_sanpham, gia, gia_giam, hinh_anh, mo_ta, so_luong, thuong_hieu, trang_thai, id_danhmuc)
VALUES
    (N'Bút bi Thiên Long TL-027', 5000, NULL, 'but_bi.jpg', N'Bút bi quốc dân cho sinh viên', 100, N'Thiên Long', 1, 1),
    (N'Vở Deli 120 trang', 12000, 10000, 'vo_deli.jpg', N'Giấy trắng, kẻ ngang chuẩn', 200, N'Deli', 1, 2),
    (N'Bút Gel Deli A067', 8000, NULL, 'but_gel_deli.jpg', N'Bút viết trơn, mực đậm', 150, N'Deli', 1, 1),
    (N'Thước kẻ HUIT 20cm', 4000, NULL, 'thuoc_huit.jpg', N'Thước nhựa dẻo in logo trường', 80, N'HUIT', 1, 3),
    (N'Combo 10 vở 200 trang', 120000, 99000, 'combo_vo.jpg', N'Giấy định lượng cao', 200, N'HUIT', 1, 2),
    (N'Máy tính Casio fx-580VNX', 650000, 620000, 'casio_580.jpg', N'Máy tính chuyên dụng học sinh sinh viên', 50, N'Casio', 1, 3),
    (N'Bút xóa kéo Thiên Long', 15000, NULL, 'xoa_keo.jpg', N'Dễ dàng sử dụng', 80, N'Thiên Long', 1, 3),
    (N'Balo sinh viên HUIT', 250000, 220000, 'balo_huit.jpg', N'Có ngăn đựng laptop', 40, N'HUIT', 1, 5),
    (N'Bút chì 2B Thiên Long', 4000, NULL, 'but_chi_2b.jpg', N'Thân gỗ bền', 120, N'Thiên Long', 1, 1),
    (N'Gôm tẩy Deli màu đen', 5000, NULL, 'gom_deli.jpg', N'Tẩy sạch, ít bụi vụn', 90, N'Deli', 1, 3),
    (N'Giấy A4 Double A 70gsm', 85000, 80000, 'giay_a4.jpg', N'Ram 500 tờ chất lượng cao', 110, N'Double A', 1, 4),
    (N'Sổ tay HUIT Limited', 45000, NULL, 'so_tay_huit.jpg', N'Bìa cứng in logo trường', 70, N'HUIT', 1, 4);

-- 5. Coupons
INSERT INTO MaGiamGia (ma_code, loai_giam, gia_tri_giam, don_toi_thieu, ngay_bat_dau, ngay_ket_thuc, so_luong, da_su_dung, trang_thai)
VALUES
    (N'SALE10', N'PERCENT', 10, 100000, '2026-01-01', '2026-12-31', 100, 0, 1),
    (N'GIAM50K', N'FIXED', 50000, 300000, '2026-01-01', '2026-12-31', 50, 0, 1),
    (N'FREESHIP', N'FIXED', 30000, 150000, '2026-01-01', '2026-12-31', 200, 0, 1);

-- 6. Sample Orders
INSERT INTO DonHang (ngay_dat, tong_tien, trang_thai, dia_chi_giao_hang, ghi_chu, phuong_thuc_thanh_toan, phi_van_chuyen, id_nguoidung)
VALUES
    ('2026-05-20 14:30:00', 47000, N'Hoàn thành', N'45 Lê Lợi, Q.1, TP.HCM', N'Giao giờ hành chính', N'COD', 30000, 2),
    ('2026-05-22 09:15:00', 220000, N'Chờ xác nhận', N'78 Trần Hưng Đạo, Q.5, TP.HCM', N'Gọi điện trước khi giao', N'COD', 0, 3);

-- 7. Sample Order Details
INSERT INTO ChiTietDonHang (so_luong, don_gia, thanh_tien, id_donhang, id_sanpham)
VALUES
    (1, 5000, 5000, 1, 1),
    (1, 12000, 12000, 1, 2),
    (1, 220000, 220000, 2, 8);

-- 8. Sample Reviews
INSERT INTO DanhGia (noi_dung, so_sao, ngay_tao, trang_thai, id_nguoidung, id_sanpham)
VALUES
    (N'Bút bi viết rất êm, ra mực đều.', 5, '2026-05-21 15:00:00', 1, 2, 1),
    (N'Vở kẻ ngang đẹp, giấy hơi mỏng.', 4, '2026-05-22 16:30:00', 1, 2, 2);
GO
