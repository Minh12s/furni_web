package com.example.furni.service;

import com.example.furni.entity.OrderCancel;
import com.example.furni.repository.OrderCancelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderCancelService {

    @Autowired
    private OrderCancelRepository orderCancelRepository;

    // Lấy tất cả các bản ghi từ bảng order_cancel
    public List<OrderCancel> getAllOrderCancels() {
        return orderCancelRepository.findAll();
    }
    public Map<String, Long> getReasonCounts() {
        List<Object[]> results = orderCancelRepository.countByReason();
        Map<String, Long> reasonCounts = new HashMap<>();
        for (Object[] result : results) {
            String reason = (String) result[0];
            Long count = (Long) result[1];
            reasonCounts.put(reason, count);
        }
        return reasonCounts;
    }
    public Page<OrderCancel> getAllOrderCancels(int page, int size) {
        return orderCancelRepository.findAll(PageRequest.of(page, size));
    }
}
