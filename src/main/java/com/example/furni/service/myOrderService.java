package com.example.furni.service;

import com.example.furni.entity.OrderReturn;
import com.example.furni.entity.Orders;
import com.example.furni.repository.OrderReturnRepository;
import com.example.furni.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class myOrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderReturnRepository orderReturnRepository;
    public Page<Orders> findOrdersByUserIdAndStatus(Integer userId, String status, Pageable pageable) {
        return ordersRepository.findByUserIdAndStatus(userId, status, pageable);
    }

    public Orders getOrderBySecureToken(String secureToken) {
        return ordersRepository.findBySecureToken(secureToken).orElse(null);
    }
    // Phương thức lấy đơn hàng theo ID
    public Orders getOrderById(int id) {
        Optional<Orders> orderOpt = ordersRepository.findById(id);
        return orderOpt.orElse(null);
    }

    public Page<OrderReturn> getOderReturnUserId(int userId, Pageable pageable) {
        return orderReturnRepository.findByUserId(userId, pageable); // Gọi phương thức với phân trang
    }


    // Phương thức lưu đơn hàng
    public void saveOrder(Orders order) {
        ordersRepository.save(order);
    }

}
