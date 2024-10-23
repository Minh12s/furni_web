package com.example.furni.service;

import com.example.furni.entity.*;
import com.example.furni.repository.OrderProductRepository;
import com.example.furni.repository.OrderReturnRepository;
import com.example.furni.repository.ProductRepository;
import com.example.furni.repository.ReturnImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    public void save(OrderReturn orderReturn) {
        orderReturnRepository.save(orderReturn);
    }

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
    public Page<OrderReturn> filterOrderReturns(int page, int size, String status, Double refundAmount, String search) {
        Pageable pageable = PageRequest.of(page, size);

        return orderReturnRepository.findByFilters(status, refundAmount, search, pageable);
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
    public void updateOrderReturnStatus(int orderReturnId, String newStatus) {
        OrderReturn orderReturn = orderReturnRepository.findById(orderReturnId)
                .orElseThrow(() -> new RuntimeException("Order return not found"));

        if ("approved".equalsIgnoreCase(newStatus) && "pending".equalsIgnoreCase(orderReturn.getStatus())) {
            // Hồi lại số lượng sản phẩm
            Product product = orderReturn.getProduct();
            product.setQty(product.getQty() + orderReturn.getQty());
            productRepository.save(product);

            // Cập nhật trạng thái trong bảng order_product
            Optional<OrderProduct> orderProductOpt = orderProductRepository.findByOrderAndProduct(
                    orderReturn.getOrder(), product);
            if (orderProductOpt.isPresent()) {
                OrderProduct orderProduct = orderProductOpt.get();
                orderProduct.setStatus(1); // Ví dụ: 1 là trạng thái "hoàn trả đã chấp nhận"
                orderProductRepository.save(orderProduct);
            }
        }

        // Cập nhật trạng thái mới cho order return
        orderReturn.setStatus(newStatus);
        orderReturnRepository.save(orderReturn);
    }
}
