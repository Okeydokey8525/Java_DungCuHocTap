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
import java.util.Optional;

@Service
public class DonHangService {
    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private ChiTietDonHangRepository chiTietRepository;

    // Lưu đơn hàng mới
    public DonHang createOrder(DonHang order) {
        return donHangRepository.save(order);
    }

    // Lấy danh sách đơn hàng cho Admin duyệt
    public List<DonHang> getAllOrders() {
        return donHangRepository.findAll();
    }

    // Lưu đơn hàng và chi tiết đơn hàng (transaction)
    @Transactional
    public DonHang saveOrder(DonHang donHang, List<ChiTietDonHang> chiTiets) {
        // 1. Lưu đơn hàng trước để lấy ID
        DonHang savedOrder = donHangRepository.save(donHang);

        // 2. Gán ID đơn hàng cho từng chi tiết và lưu
        for (ChiTietDonHang ct : chiTiets) {
            ct.setDonHang(savedOrder);
            chiTietRepository.save(ct);
        }
        return savedOrder;
    }

    // Lấy danh sách đơn hàng của người dùng
    public List<DonHang> getOrdersByUser(NguoiDung user) {
        return donHangRepository.findByNguoiDung(user);
    }

    // Lấy đơn hàng theo ID
    public DonHang getOrderById(Integer orderId) {
        Optional<DonHang> order = donHangRepository.findById(orderId);
        return order.orElse(null);
    }

    // Cập nhật đơn hàng
    public DonHang updateOrder(DonHang order) {
        return donHangRepository.save(order);
    }

    // Lấy chi tiết đơn hàng theo ID đơn hàng
    public List<ChiTietDonHang> getOrderDetails(Integer orderId) {
        return chiTietRepository.findByDonHangId_donhang(orderId);
    }

    // Xóa đơn hàng (admin function)
    @Transactional
    public void deleteOrder(Integer orderId) {
        // Xóa chi tiết đơn hàng trước
        chiTietRepository.deleteByDonHangId_donhang(orderId);
        // Sau đó xóa đơn hàng
        donHangRepository.deleteById(orderId);
    }

    // Cập nhật trạng thái đơn hàng
    public DonHang updateOrderStatus(Integer orderId, String status) {
        Optional<DonHang> orderOpt = donHangRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            DonHang order = orderOpt.get();
            order.setTrang_thai(status);
            return donHangRepository.save(order);
        }
        return null;
    }

    // Đếm số đơn hàng của người dùng
    public long countOrdersByUser(NguoiDung user) {
        return donHangRepository.countByNguoiDung(user);
    }

    // Tính tổng doanh thu của người dùng
    public Double getTotalRevenueByUser(NguoiDung user) {
        List<DonHang> orders = donHangRepository.findByNguoiDung(user);
        return orders.stream()
                .mapToDouble(DonHang::getTong_tien)
                .sum();
    }
}