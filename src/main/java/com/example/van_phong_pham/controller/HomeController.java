package com.example.van_phong_pham.controller;


import com.example.van_phong_pham.model.SanPham;
import com.example.van_phong_pham.service.DanhMucService;
import com.example.van_phong_pham.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SanPhamService sanPhamService;
    private final DanhMucService danhMucService;

    @GetMapping({"/", "/home"})
    public String index(Model model) {
        model.addAttribute("listSanPham", sanPhamService.getAllProducts());
        model.addAttribute("listDanhMuc", danhMucService.getAllCategories());
        return "home";
    }

    @GetMapping("/san-pham/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        SanPham sp = sanPhamService.findById(id);
        model.addAttribute("sanPham", sp);
        return "chi-tiet-san-pham";
    }
}
