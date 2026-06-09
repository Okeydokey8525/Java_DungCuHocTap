package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.*;
import com.example.van_phong_pham.service.*;
import com.example.van_phong_pham.repository.DonHangRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SanPhamService sanPhamService;
    private final DanhMucService danhMucService;
    private final DonHangService donHangService;
    private final NguoiDungService nguoiDungService;
    private final DanhGiaService danhGiaService;
    private final MaGiamGiaService maGiamGiaService;
    private final FileUploadService fileUploadService;
    private final DonHangRepository donHangRepository;

    // --- 1. DASHBOARD ---
    @GetMapping({"", "/dashboard"})
    public String dashboard(Model model) {
        Double totalRevenue = donHangRepository.getTotalRevenue();
        long totalOrders = donHangRepository.count();
        long totalUsers = nguoiDungService.countUsers();
        
        List<SanPham> lowStockProducts = sanPhamService.getLowStockProducts();
        long lowStockCount = lowStockProducts.size();

        List<DonHang> recentOrders = donHangRepository.findTop10ByOrderByNgayDatDesc();

        model.addAttribute("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("activePage", "dashboard");

        return "admin/dashboard";
    }

    // --- 2. SẢN PHẨM ---
    @GetMapping("/san-pham")
    public String quanLySanPham(
            @RequestParam(required = false) Integer editId,
            Model model) {
        
        List<SanPham> products = sanPhamService.getAllProducts();
        List<DanhMuc> categories = danhMucService.getAll();
        
        if (editId != null) {
            SanPham editProduct = sanPhamService.getProductById(editId);
            model.addAttribute("editProduct", editProduct);
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("activePage", "san-pham");
        return "admin/quan-ly-san-pham";
    }

    @PostMapping("/san-pham/save")
    public String saveSanPham(
            @ModelAttribute SanPham sanPham,
            @RequestParam("danhMucId") Integer danhMucId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Set danh mục
            DanhMuc dm = danhMucService.findById(danhMucId);
            sanPham.setDanhMuc(dm);

            // Xử lý upload ảnh
            if (imageFile != null && !imageFile.isEmpty()) {
                // Xóa ảnh cũ nếu có
                if (sanPham.getId_sanpham() != null) {
                    SanPham oldProduct = sanPhamService.getProductById(sanPham.getId_sanpham());
                    if (oldProduct != null && oldProduct.getHinh_anh() != null) {
                        fileUploadService.deleteFile(oldProduct.getHinh_anh());
                    }
                }
                String imagePath = fileUploadService.uploadFile(imageFile);
                sanPham.setHinh_anh(imagePath);
            } else if (sanPham.getId_sanpham() != null) {
                // Giữ nguyên ảnh cũ nếu không upload ảnh mới
                SanPham oldProduct = sanPhamService.getProductById(sanPham.getId_sanpham());
                if (oldProduct != null) {
                    sanPham.setHinh_anh(oldProduct.getHinh_anh());
                }
            }

            sanPhamService.saveProduct(sanPham);
            redirectAttributes.addFlashAttribute("success", "Lưu sản phẩm thành công!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi upload hình ảnh: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/admin/san-pham";
    }

    @GetMapping("/san-pham/delete")
    public String deleteSanPham(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            SanPham sp = sanPhamService.getProductById(id);
            if (sp != null && sp.getHinh_anh() != null) {
                fileUploadService.deleteFile(sp.getHinh_anh());
            }
            sanPhamService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa sản phẩm: " + e.getMessage());
        }
        return "redirect:/admin/san-pham";
    }

    // --- 3. DANH MỤC ---
    @GetMapping("/danh-muc")
    public String quanLyDanhMuc(
            @RequestParam(required = false) Integer editId,
            Model model) {
        
        List<DanhMuc> categories = danhMucService.getAll();
        
        if (editId != null) {
            DanhMuc editCat = danhMucService.findById(editId);
            model.addAttribute("editCat", editCat);
        }

        model.addAttribute("categories", categories);
        model.addAttribute("activePage", "danh-muc");
        return "admin/quan-ly-danh-muc";
    }

    @PostMapping("/danh-muc/save")
    public String saveDanhMuc(@ModelAttribute DanhMuc danhMuc, RedirectAttributes redirectAttributes) {
        try {
            danhMucService.save(danhMuc);
            redirectAttributes.addFlashAttribute("success", "Lưu danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi lưu danh mục: " + e.getMessage());
        }
        return "redirect:/admin/danh-muc";
    }

    @GetMapping("/danh-muc/delete")
    public String deleteDanhMuc(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            danhMucService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa danh mục: " + e.getMessage());
        }
        return "redirect:/admin/danh-muc";
    }

    // --- 4. ĐƠN HÀNG ---
    @GetMapping("/don-hang")
    public String quanLyDonHang(
            @RequestParam(required = false, defaultValue = "Tất cả") String status,
            Model model) {
        
        List<DonHang> orders;
        if ("Tất cả".equals(status)) {
            orders = donHangService.getAllOrders();
        } else {
            orders = donHangRepository.findByTrang_thai(status);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("activePage", "don-hang");
        return "admin/quan-ly-don-hang";
    }

    @GetMapping("/don-hang/detail")
    public String chiTietDonHang(@RequestParam Integer id, Model model, RedirectAttributes redirectAttributes) {
        DonHang order = donHangService.getOrderById(id);
        if (order == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
            return "redirect:/admin/don-hang";
        }

        model.addAttribute("order", order);
        model.addAttribute("orderDetails", donHangService.getOrderDetails(id));
        model.addAttribute("activePage", "don-hang");
        return "admin/chi-tiet-don-hang";
    }

    @PostMapping("/don-hang/update-status")
    public String updateOrderStatus(
            @RequestParam Integer orderId,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {
        try {
            DonHang order = donHangService.getOrderById(orderId);
            if (order != null) {
                // Ràng buộc luồng chuyển trạng thái logic
                String currentStatus = order.getTrang_thai();
                boolean validTransition = false;

                if ("Chờ xác nhận".equals(currentStatus) && ("Đã xác nhận".equals(status) || "Đã hủy".equals(status))) {
                    validTransition = true;
                } else if ("Đã xác nhận".equals(currentStatus) && ("Đang giao".equals(status) || "Đã hủy".equals(status))) {
                    validTransition = true;
                } else if ("Đang giao".equals(currentStatus) && "Hoàn thành".equals(status)) {
                    validTransition = true;
                }

                if (validTransition) {
                    donHangService.updateOrderStatus(orderId, status);
                    redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái đơn hàng thành công!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Chuyển đổi trạng thái từ '" + currentStatus + "' sang '" + status + "' là không hợp lệ!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn hàng!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật đơn hàng: " + e.getMessage());
        }
        return "redirect:/admin/don-hang";
    }

    // --- 5. NGƯỜI DÙNG ---
    @GetMapping("/nguoi-dung")
    public String quanLyNguoiDung(Model model) {
        model.addAttribute("users", nguoiDungService.getAllUsers());
        model.addAttribute("activePage", "nguoi-dung");
        return "admin/quan-ly-nguoi-dung";
    }

    @GetMapping("/nguoi-dung/toggle")
    public String toggleNguoiDung(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            nguoiDungService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái tài khoản thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/nguoi-dung";
    }

    // --- 6. ĐÁNH GIÁ ---
    @GetMapping("/danh-gia")
    public String quanLyDanhGia(Model model) {
        model.addAttribute("reviews", danhGiaService.getAllReviews());
        model.addAttribute("activePage", "danh-gia");
        return "admin/quan-ly-danh-gia";
    }

    @GetMapping("/danh-gia/toggle")
    public String toggleDanhGiaStatus(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            danhGiaService.toggleStatus(id);
            redirectAttributes.addFlashAttribute("success", "Cập nhật trạng thái đánh giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/danh-gia";
    }

    @GetMapping("/danh-gia/delete")
    public String deleteDanhGia(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            danhGiaService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Xóa đánh giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi xóa đánh giá: " + e.getMessage());
        }
        return "redirect:/admin/danh-gia";
    }

    // --- 7. KHUYẾN MÃI ---
    @GetMapping("/khuyen-mai")
    public String quanLyKhuyenMai(
            @RequestParam(required = false) Integer editId,
            Model model) {
        
        List<MaGiamGia> coupons = maGiamGiaService.getAllCoupons();
        
        if (editId != null) {
            MaGiamGia editCoupon = maGiamGiaService.findById(editId);
            model.addAttribute("editCoupon", editCoupon);
            
            // Format ngày để đưa vào input datetime-local của HTML5
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            if (editCoupon.getNgayBatDau() != null) {
                model.addAttribute("editCouponNgayBatDau", editCoupon.getNgayBatDau().format(formatter));
            }
            if (editCoupon.getNgayKetThuc() != null) {
                model.addAttribute("editCouponNgayKetThuc", editCoupon.getNgayKetThuc().format(formatter));
            }
        }

        model.addAttribute("coupons", coupons);
        model.addAttribute("activePage", "khuyen-mai");
        return "admin/quan-ly-khuyen-mai";
    }

    @PostMapping("/khuyen-mai/save")
    public String saveKhuyenMai(
            @ModelAttribute MaGiamGia coupon,
            @RequestParam(value = "ngayBatDauStr", required = false) String ngayBatDauStr,
            @RequestParam(value = "ngayKetThucStr", required = false) String ngayKetThucStr,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Parse ngày
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            if (ngayBatDauStr != null && !ngayBatDauStr.isEmpty()) {
                coupon.setNgayBatDau(LocalDateTime.parse(ngayBatDauStr, formatter));
            }
            if (ngayKetThucStr != null && !ngayKetThucStr.isEmpty()) {
                coupon.setNgayKetThuc(LocalDateTime.parse(ngayKetThucStr, formatter));
            }

            maGiamGiaService.saveCoupon(coupon);
            redirectAttributes.addFlashAttribute("success", "Lưu mã giảm giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi lưu mã giảm giá: " + e.getMessage());
        }
        return "redirect:/admin/khuyen-mai";
    }

    @GetMapping("/khuyen-mai/delete")
    public String deleteKhuyenMai(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            maGiamGiaService.deleteCoupon(id);
            redirectAttributes.addFlashAttribute("success", "Xóa mã giảm giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa mã giảm giá: " + e.getMessage());
        }
        return "redirect:/admin/khuyen-mai";
    }
}
