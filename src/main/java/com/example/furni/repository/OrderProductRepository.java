package com.example.furni.repository;

import com.example.furni.entity.OrderProduct;
import com.example.furni.entity.Orders;
import com.example.furni.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
    List<OrderProduct> findByOrderId(int orderId);
    boolean existsByProductId(int productId);

    Optional<OrderProduct> findByOrderAndProduct(Orders order, Product product);
}
