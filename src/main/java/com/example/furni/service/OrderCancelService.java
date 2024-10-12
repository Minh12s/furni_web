package com.example.furni.service;

import com.example.furni.entity.Orders;
import com.example.furni.repository.OrdersRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderCancelService {

    @Autowired
    private OrdersRepository orderRepository;

    public List<Orders> getAllOrderCancels() {
        return orderRepository.findAll();
    }
    public Map<String, Long> getReasonCounts() {
        List<Object[]> results = orderRepository.countByReason();
        Map<String, Long> reasonCounts = new HashMap<>();
        for (Object[] result : results) {
            String reason = (String) result[0];
            Long count = (Long) result[1];
            reasonCounts.put(reason, count);
        }
        return reasonCounts;
    }

    public Page<Orders> filterOrderCancels(int page, int size, String email, String telephone, Double totalAmount, String search) {
        // Tạo Pageable object
        Pageable pageable = PageRequest.of(page, size);

        // Thực hiện logic tìm kiếm và lọc
        if (email != null || telephone != null || totalAmount != null || search != null) {
            return orderRepository.findByFilters(email, telephone, totalAmount, search, pageable);
        } else {
            return orderRepository.findByStatus("cancel", pageable);
        }
    }
    public Page<Orders> getOtherCancelReasons(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<String> popularReasons = List.of("Order takes a long time to confirm", "I don't need to buy anymore",
                "I want to update my shipping address", "I found a better place to buy (Cheaper, more reputable, faster delivery...)",
                "I don't have enough money to buy it");
        return orderRepository.findByCancelReasonNotIn(popularReasons, pageable);
    }

}
