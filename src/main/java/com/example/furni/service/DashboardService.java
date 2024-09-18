package com.example.furni.service;

import com.example.furni.entity.Orders;
import com.example.furni.entity.Review;
import com.example.furni.repository.ProductRepository;
import com.example.furni.repository.ReviewRepository;
import com.example.furni.repository.UserRepository;
import com.example.furni.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    // lấy tổng tất cả khách hàng , trừ admin
    public long getTotalUsers() {
        return userRepository.countUsersExcludingAdmin();
    }
    // lấy tổng số tiền của tất cả các đơn hàng
    public double getTotalOrderAmount() {
        Double totalAmount = ordersRepository.getTotalAmountOfCompletedOrders();
        return totalAmount != null ? totalAmount : 0.0;
    }
    // Tính tổng giá trị tất cả sản phẩm
    public long getTotalProducts() {
        return productRepository.count();
    }
    // Lấy danh sách các đơn hàng có trạng thái là 'pending'
    public Page<Orders> getPendingOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ordersRepository.findPendingOrders(pageable);
    }
    // Lấy danh sách các review có trạng thái 'pending' và phân trang
    public Page<Review> getPendingReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reviewRepository.findByStatus("pending", pageable);
    }
}
