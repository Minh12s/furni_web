package com.example.furni.repository;

import com.example.furni.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUserId(int userId);
    @Query("SELECT o FROM Orders o WHERE " +
            "(:ShippingMethod IS NULL OR o.shippingMethod LIKE %:ShippingMethod%) AND " +
            "(:TotalAmount IS NULL OR o.totalAmount = :TotalAmount) AND " +
            "(:PaymentMethod IS NULL OR o.paymentMethod LIKE %:PaymentMethod%) AND " +
            "(:IsPaid IS NULL OR o.isPaid = :IsPaid) AND " +
            "(:Status IS NULL OR o.status = :Status)")
    Page<Orders> filterOrders(@Param("ShippingMethod") String ShippingMethod,
                              @Param("TotalAmount") Double TotalAmount,
                              @Param("PaymentMethod") String PaymentMethod,
                              @Param("IsPaid") String IsPaid,
                              @Param("Status") String Status,
                              Pageable pageable);
}
