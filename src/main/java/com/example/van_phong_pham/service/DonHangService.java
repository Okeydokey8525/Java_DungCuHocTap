package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.ChiTietDonHang;
import com.example.van_phong_pham.model.DonHang;
import com.example.van_phong_pham.model.NguoiDung;
import com.example.van_phong_pham.repository.ChiTietDonHangRepository;
import com.example.van_phong_pham.repository.DonHangRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangService {
    @Autowired
    private DonHangRepository donHangRepository;

    // Lưu đơn hàng mới (Thành viên 3 sẽ gọi hàm này)
    public DonHang createOrder(DonHang order) {
        return donHangRepository.save(order);
    }

    // Lấy danh sách đơn hàng cho Admin duyệt (Thành viên 4 sẽ dùng)
    public List<DonHang> getAllOrders() {
        return donHangRepository.findAll();
    }


    @Autowired
    private ChiTietDonHangRepository chiTietRepository;

    @Transactional // Đảm bảo nếu lưu lỗi thì sẽ hoàn tác (Rollback)
    public void saveOrder(DonHang donHang, List<ChiTietDonHang> chiTiets) {
        // 1. Lưu đơn hàng trước để lấy ID
        DonHang savedOrder = donHangRepository.save(donHang);

        // 2. Gán ID đơn hàng cho từng chi tiết và lưu
        for (ChiTietDonHang ct : chiTiets) {
            ct.setDonHang(savedOrder);
            chiTietRepository.save(ct);
        }
    }

    public List<DonHang> getOrdersByUser(NguoiDung user) {
        return donHangRepository.findByNguoiDung(user);
    }
}