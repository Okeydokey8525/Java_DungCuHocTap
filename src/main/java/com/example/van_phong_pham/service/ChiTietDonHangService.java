package com.example.van_phong_pham.service;

import com.example.van_phong_pham.model.ChiTietDonHang;
import com.example.van_phong_pham.repository.ChiTietDonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ChiTietDonHangService {
    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

    // Lưu từng món hàng vào chi tiết đơn hàng
    public void saveDetail(ChiTietDonHang detail) {
        chiTietDonHangRepository.save(detail);
    }

    // Xem chi tiết các món hàng của một đơn hàng cụ thể
    public List<ChiTietDonHang> getDetailsByOrderId(Integer orderId) {
        // Sau này bạn có thể viết thêm logic tìm kiếm tại đây
        return chiTietDonHangRepository.findAll();
    }
}
