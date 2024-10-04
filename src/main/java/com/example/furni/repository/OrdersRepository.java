package com.example.furni.repository;

import com.example.furni.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByUserId(int userId);
    Page<Orders> findOderByUserId(int userId, Pageable pageable); // Thêm tham số Pageable

    @Query("SELECT o FROM Orders o WHERE " +
            "(:ShippingMethod IS NULL OR o.shippingMethod LIKE %:ShippingMethod%) AND " +
            "(:TotalAmount IS NULL OR o.totalAmount = :TotalAmount) AND " +
            "(:PaymentMethod IS NULL OR o.paymentMethod LIKE %:PaymentMethod%) AND " +
            "(:IsPaid IS NULL OR o.isPaid = :IsPaid) AND " +
            "(:Status IS NULL OR o.status = :Status) " +
            "ORDER BY CASE WHEN o.status = 'pending' THEN 0 ELSE 1 END, o.orderDate DESC")
    Page<Orders> filterOrders(@Param("ShippingMethod") String ShippingMethod,
                              @Param("TotalAmount") Double TotalAmount,
                              @Param("PaymentMethod") String PaymentMethod,
                              @Param("IsPaid") String IsPaid,
                              @Param("Status") String Status,
                              Pageable pageable);

    // tính tổng total amount tất cả đơn hàng có status là complete
    @Query("SELECT SUM(o.totalAmount) FROM Orders o WHERE o.status = 'complete'")
    Double getTotalAmountOfCompletedOrders();
    // Lấy các đơn hàng có status là 'pending'
    @Query("SELECT o FROM Orders o WHERE o.status = 'pending'")
    Page<Orders> findPendingOrders(Pageable pageable);

    // thống kê doanh số theo năm
    @Query("SELECT MONTH(o.orderDate), SUM(op.qty) " +
            "FROM Orders o JOIN OrderProduct op ON o.id = op.order.id " +
            "WHERE o.status = 'complete' AND YEAR(o.orderDate) = :year " +
            "GROUP BY MONTH(o.orderDate) " +
            "ORDER BY MONTH(o.orderDate)")
    List<Object[]> findCompletedOrdersByYear(@Param("year") int year);
    // thống kê doanh thu theo năm
    @Query("SELECT MONTH(o.orderDate), SUM(o.totalAmount) " +
            "FROM Orders o " +
            "WHERE o.status = 'complete' AND YEAR(o.orderDate) = :year " +
            "GROUP BY MONTH(o.orderDate) " +
            "ORDER BY MONTH(o.orderDate)")
    List<Object[]> findTotalRevenueByYear(@Param("year") int year);
    // Thống kê doanh số theo ngày
    @Query("SELECT o.orderDate, SUM(op.qty) " +
            "FROM Orders o JOIN OrderProduct op ON o.id = op.order.id " +
            "WHERE o.status = 'complete' AND o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY o.orderDate " +
            "ORDER BY o.orderDate")
    List<Object[]> findProductsSoldByDate(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    // Thống kê doanh thu theo ngày
    @Query("SELECT o.orderDate, SUM(o.totalAmount) " +
            "FROM Orders o " +
            "WHERE o.status = 'complete' AND o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY o.orderDate " +
            "ORDER BY o.orderDate")
    List<Object[]> findRevenueByDate(@Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate);
    // thống kê biểu đò tròn tính số lượng status order
    @Query("SELECT o.status, COUNT(o) FROM Orders o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();
    Orders findFirstByOrderByIdDesc();

}
