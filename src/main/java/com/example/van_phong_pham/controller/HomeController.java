package com.example.van_phong_pham.controller;

import com.example.van_phong_pham.model.DanhGia;
import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.model.SanPham;
import com.example.van_phong_pham.service.DanhMucService;
import com.example.van_phong_pham.service.SanPhamService;
import com.example.van_phong_pham.service.DanhGiaService;
import com.example.van_phong_pham.service.NguoiDungService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SanPhamService sanPhamService;
    private final DanhMucService danhMucService;
    private final DanhGiaService danhGiaService;
    private final NguoiDungService nguoiDungService;

    @GetMapping({"/", "/home"})
    public String index(Model model) {
        model.addAttribute("listSanPham", sanPhamService.getAllProducts());
        model.addAttribute("listDanhMuc", danhMucService.getAllCategories());
        model.addAttribute("saleProducts", sanPhamService.getSaleProducts());
        return "home";
    }

    @GetMapping("/san-pham/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        SanPham sp = sanPhamService.findById(id);
        if (sp != null) {
            model.addAttribute("sanPham", sp);
            model.addAttribute("reviews", danhGiaService.getApprovedReviews(sp));
            model.addAttribute("averageRating", danhGiaService.getAverageRating(id));
            model.addAttribute("reviewCount", danhGiaService.countReviews(id));
        }
        return "chi-tiet-san-pham";
    }

    @PostMapping("/san-pham/danh-gia")
    public String saveDanhGia(Principal principal,
                               @RequestParam Integer id_sanpham,
                               @RequestParam Integer so_sao,
                               @RequestParam String noi_dung,
                               RedirectAttributes redirectAttributes) {
        if (principal == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để viết đánh giá!");
            return "redirect:/login";
        }
        try {
            NguoiDung user = nguoiDungService.findByEmail(principal.getName());
            SanPham sp = sanPhamService.findById(id_sanpham);
            if (sp == null) {
                redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại.");
                return "redirect:/home";
            }
            
            DanhGia dg = new DanhGia();
            dg.setSanPham(sp);
            dg.setNguoiDung(user);
            dg.setSo_sao(so_sao);
            dg.setNoi_dung(noi_dung);
            dg.setTrang_thai(true); // Tự động duyệt luôn
            
            danhGiaService.saveReview(dg);
            redirectAttributes.addFlashAttribute("success", "Cảm ơn bạn đã đánh giá sản phẩm!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi gửi đánh giá: " + e.getMessage());
        }
        return "redirect:/san-pham/" + id_sanpham;
    }

    @GetMapping("/san-pham/tim-kiem")
    public String timKiem(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer danhMuc,
            @RequestParam(required = false) Double giaMin,
            @RequestParam(required = false) Double giaMax,
            @RequestParam(required = false) String thuongHieu,
            @RequestParam(required = false, defaultValue = "newest") String sort,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "16") int size,
            Model model) {
        
        Page<SanPham> pageResult = sanPhamService.searchProducts(
                keyword != null && !keyword.trim().isEmpty() ? keyword.trim() : null,
                danhMuc,
                giaMin,
                giaMax,
                thuongHieu != null && !thuongHieu.trim().isEmpty() ? thuongHieu.trim() : null,
                page,
                size,
                sort
        );
        
        model.addAttribute("products", pageResult.getContent());
        model.addAttribute("categories", danhMucService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("danhMucId", danhMuc);
        model.addAttribute("giaMin", giaMin);
        model.addAttribute("giaMax", giaMax);
        model.addAttribute("thuongHieu", thuongHieu);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());
        model.addAttribute("totalElements", pageResult.getTotalElements());
        
        return "danh-sach-san-pham";
    }

    @GetMapping("/gioi-thieu")
    public String gioiThieu() {
        return "gioi-thieu";
    }

    @GetMapping("/lien-he")
    public String lienHe() {
        return "lien-he";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }

    @GetMapping("/404")
    public String notFound() {
        return "404";
    }
}
