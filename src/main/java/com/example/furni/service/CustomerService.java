package com.example.furni.service;

import com.example.furni.entity.Orders;
import com.example.furni.entity.OrderProduct;
import com.example.furni.entity.User;
import com.example.furni.repository.OrderProductRepository;
import com.example.furni.repository.OrdersRepository;
import com.example.furni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    // customer management
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Page<User> getCustomersWithPaging(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    public Page<User> getFilteredCustomers(String userName, String address, String phoneNumber, String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByUserNameContainingAndAddressContainingAndPhoneNumberContainingAndEmailContaining(
                userName, address, phoneNumber, email, pageable);
    }

    // Lấy danh sách đơn hàng của user
    public List<Orders> getOrdersByUserId(int userId) {
        return ordersRepository.findByUserId(userId);
    }

    // Lấy chi tiết sản phẩm trong một đơn hàng
    public Orders getOrderById(int id) {
        return ordersRepository.findById(id).orElse(null);
    }
    // Lấy danh sách sản phẩm theo orderId
    public List<OrderProduct> getOrderProductsByOrderId(int orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }
}
