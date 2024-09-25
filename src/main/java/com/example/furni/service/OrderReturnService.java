package com.example.furni.service;

import com.example.furni.entity.Material;
import com.example.furni.entity.OrderReturn;
import com.example.furni.entity.ReturnImages;
import com.example.furni.entity.Review;
import com.example.furni.repository.OrderReturnRepository;
import com.example.furni.repository.ReturnImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderReturnService {

    @Autowired
    private OrderReturnRepository orderReturnRepository;

    @Autowired
    private ReturnImagesRepository returnImagesRepository;

    // Lấy tất cả OrderReturn cùng với ảnh liên quan
    public List<OrderReturn> getAllOrderReturns() {
        List<OrderReturn> orderReturns = orderReturnRepository.findAll();
        for (OrderReturn orderReturn : orderReturns) {
            // Lấy các ReturnImages cho mỗi OrderReturn
            List<ReturnImages> returnImages = returnImagesRepository.findByOrderReturnId(orderReturn.getId());
            orderReturn.setReturnImages(returnImages);
        }
        return orderReturns;
    }

    // Lấy OrderReturn theo ID cùng với ảnh liên quan
    public OrderReturn getOrderReturnById(int id) {
        OrderReturn orderReturn = orderReturnRepository.findById(id).orElse(null);
        if (orderReturn != null) {
            List<ReturnImages> returnImages = returnImagesRepository.findByOrderReturnId(id);
            orderReturn.setReturnImages(returnImages);
        }
        return orderReturn;
    }
    public Page<OrderReturn> getOrderReturnPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderReturnRepository.findAll(pageable);
    }
    public Map<String, Long> getReasonCounts() {
        List<Object[]> results = orderReturnRepository.countByReason();
        Map<String, Long> reasonCounts = new HashMap<>();
        for (Object[] result : results) {
            String reason = (String) result[0];
            Long count = (Long) result[1];
            reasonCounts.put(reason, count);
        }
        return reasonCounts;
    }
    // Cập nhật trạng thái đơn trả hàng
    public boolean updateOrderReturnStatus(int orderReturnId, String status) {
        Optional<OrderReturn> OrderReturnOptional = orderReturnRepository.findById(orderReturnId);
        if (OrderReturnOptional.isPresent()) {
            OrderReturn orderReturn = OrderReturnOptional.get();
            if ("pending".equals(orderReturn.getStatus())) {
                orderReturn.setStatus(status);  // Update status to "approved" or "rejected"
                orderReturnRepository.save(orderReturn);
                return true;
            }
        }
        return false;
    }
}
