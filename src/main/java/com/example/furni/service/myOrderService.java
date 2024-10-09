package com.example.furni.service;

import com.example.furni.entity.Orders;
import com.example.furni.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class myOrderService {

    @Autowired
    private OrdersRepository ordersRepository;
    public Page<Orders> findOrdersByUserIdAndStatus(Integer userId, String status, Pageable pageable) {
        return ordersRepository.findByUserIdAndStatus(userId, status, pageable);
    }

}
