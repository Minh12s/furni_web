//package com.example.furni.service;
//
//import com.example.furni.entity.OrderCancel;
//import com.example.furni.repository.OrderCancelRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class OrderCancelService {
//
//    @Autowired
//    private OrderCancelRepository orderCancelRepository;
//
//    // Lấy tất cả các bản ghi từ bảng order_cancel
//    public List<OrderCancel> getAllOrderCancels() {
//        return orderCancelRepository.findAll();
//    }
//    public Map<String, Long> getReasonCounts() {
//        List<Object[]> results = orderCancelRepository.countByReason();
//        Map<String, Long> reasonCounts = new HashMap<>();
//        for (Object[] result : results) {
//            String reason = (String) result[0];
//            Long count = (Long) result[1];
//            reasonCounts.put(reason, count);
//        }
//        return reasonCounts;
//    }
//    public Page<OrderCancel> getAllOrderCancels(int page, int size) {
//        return orderCancelRepository.findAll(PageRequest.of(page, size));
//    }
//    public Page<OrderCancel> filterOrderCancels(int page, int size, String email, String telephone, Double totalAmount, String search) {
//        // Tạo Pageable object
//        Pageable pageable = PageRequest.of(page, size);
//
//        // Thực hiện logic tìm kiếm và lọc
//        if (email != null || telephone != null || totalAmount != null || search != null) {
//            return orderCancelRepository.findByFilters(email, telephone, totalAmount, search, pageable);
//        } else {
//            return orderCancelRepository.findAll(pageable);
//        }
//    }
//
//}
