package com.example.van_phong_pham.config;

import com.example.van_phong_pham.model.DanhMuc;
import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.SanPham;
import com.example.van_phong_pham.model.VaiTro;
import com.example.van_phong_pham.repository.DanhMucRepository;
import com.example.van_phong_pham.repository.NguoiDungRepository;
import com.example.van_phong_pham.repository.SanPhamRepository;
import com.example.van_phong_pham.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Initialize Roles
        VaiTro adminRole = vaiTroRepository.findByTen_vaitro("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new VaiTro();
            adminRole.setTen_vaitro("ROLE_ADMIN");
            adminRole = vaiTroRepository.save(adminRole);
        }

        VaiTro userRole = vaiTroRepository.findByTen_vaitro("ROLE_USER");
        if (userRole == null) {
            userRole = new VaiTro();
            userRole.setTen_vaitro("ROLE_USER");
            userRole = vaiTroRepository.save(userRole);
        }

        // 2. Initialize Admin & User accounts
        if (nguoiDungRepository.findByEmail("admin@huit.edu.vn") == null) {
            NguoiDung admin = new NguoiDung();
            admin.setHo_ten("Admin HUIT");
            admin.setEmail("admin@huit.edu.vn");
            admin.setMat_khau(passwordEncoder.encode("123456"));
            admin.setSo_dien_thoai("0123456789");
            admin.setDia_chi("12 Nguyễn Văn Bảo, Q. Gò Vấp, TP.HCM");
            admin.setTrang_thai(true);
            admin.setNgayTao(LocalDateTime.now());
            admin.setVaiTro(adminRole);
            nguoiDungRepository.save(admin);
        }

        if (nguoiDungRepository.findByEmail("user@gmail.com") == null) {
            NguoiDung user = new NguoiDung();
            user.setHo_ten("Nguyễn Văn A");
            user.setEmail("user@gmail.com");
            user.setMat_khau(passwordEncoder.encode("123456"));
            user.setSo_dien_thoai("0987654321");
            user.setDia_chi("45 Lê Lợi, Q.1, TP.HCM");
            user.setTrang_thai(true);
            user.setNgayTao(LocalDateTime.now());
            user.setVaiTro(userRole);
            nguoiDungRepository.save(user);
        }

        // 3. Initialize Categories
        if (danhMucRepository.count() == 0) {
            DanhMuc dm1 = createCategory("Bút viết");
            DanhMuc dm2 = createCategory("Tập vở");
            DanhMuc dm3 = createCategory("Dụng cụ học tập");
            DanhMuc dm4 = createCategory("Văn phòng phẩm");
            DanhMuc dm5 = createCategory("Balo & Túi");

            // 4. Initialize Sample Products
            if (sanPhamRepository.count() == 0) {
                createProduct("Bút bi Thiên Long TL-027", 5000.0, null, "https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?w=500", "Bút bi quốc dân cho sinh viên", 100, "Thiên Long", dm1);
                createProduct("Vở Deli 120 trang", 12000.0, 10000.0, "https://images.unsplash.com/photo-1531346878377-a5be20888e57?w=500", "Giấy trắng, kẻ ngang chuẩn", 200, "Deli", dm2);
                createProduct("Bút Gel Deli A067", 8000.0, null, "https://images.unsplash.com/photo-1511556532299-8f662fc26c06?w=500", "Bút viết trơn, mực đậm", 150, "Deli", dm1);
                createProduct("Thước kẻ HUIT 20cm", 4000.0, null, "https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?w=500", "Thước nhựa dẻo in logo trường", 80, "HUIT", dm3);
                createProduct("Combo 10 vở 200 trang", 120000.0, 99000.0, "https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=500", "Giấy định lượng cao", 200, "HUIT", dm2);
                createProduct("Máy tính Casio fx-580VNX", 650000.0, 620000.0, "https://images.unsplash.com/photo-1574634534894-89d7576c8259?w=500", "Máy tính chuyên dụng học sinh sinh viên", 50, "Casio", dm3);
                createProduct("Bút xóa kéo Thiên Long", 15000.0, null, "https://images.unsplash.com/photo-1516962215378-7fa2e137ae93?w=500", "Dễ dàng sử dụng", 80, "Thiên Long", dm3);
                createProduct("Balo sinh viên HUIT", 250000.0, 220000.0, "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500", "Có ngăn đựng laptop", 40, "HUIT", dm5);
                createProduct("Bút chì 2B Thiên Long", 4000.0, null, "https://images.unsplash.com/photo-1519750783826-e2420f4d687f?w=500", "Thân gỗ bền", 120, "Thiên Long", dm1);
                createProduct("Gôm tẩy Deli màu đen", 5000.0, null, "https://images.unsplash.com/photo-1586075010923-2dd4570fb338?w=500", "Tẩy sạch, ít bụi vụn", 90, "Deli", dm3);
                createProduct("Giấy A4 Double A 70gsm", 85000.0, 80000.0, "https://images.unsplash.com/photo-1586075010923-2dd4570fb338?w=500", "Ram 500 tờ chất lượng cao", 110, "Double A", dm4);
                createProduct("Sổ tay HUIT Limited", 45000.0, null, "https://images.unsplash.com/photo-1544816155-12df9643f363?w=500", "Bìa cứng in logo trường", 70, "HUIT", dm4);
            }
        }
    }

    private DanhMuc createCategory(String name) {
        DanhMuc dm = new DanhMuc();
        dm.setTen_danhmuc(name);
        return danhMucRepository.save(dm);
    }

    private void createProduct(String name, Double price, Double salePrice, String image, String description, Integer qty, String brand, DanhMuc cat) {
        SanPham sp = new SanPham();
        sp.setTen_sanpham(name);
        sp.setGia(price);
        sp.setGia_giam(salePrice);
        sp.setHinh_anh(image);
        sp.setMo_ta(description);
        sp.setSo_luong(qty);
        sp.setThuong_hieu(brand);
        sp.setTrang_thai(true);
        sp.setNgayTao(LocalDateTime.now());
        sp.setDanhMuc(cat);
        sanPhamRepository.save(sp);
    }
}
