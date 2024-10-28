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
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Optional<Orders> findBySecureToken(String secureToken);

    List<Orders> findByUserId(int userId);
    Page<Orders> findOderByUserId(int userId, Pageable pageable); // Thêm tham số Pageable
    // Lấy các đơn hàng theo trạng thái
    Page<Orders> findByStatus(String status, Pageable pageable);

    Page<Orders> findByUserIdAndStatus(Integer userId, String status, Pageable pageable);

    @Query("SELECT o.cancelReason, COUNT(o) FROM Orders o WHERE o.status = 'cancel' GROUP BY o.cancelReason")
    List<Object[]> countByReason();
    // tìm các lý do khác
    @Query("SELECT o FROM Orders o WHERE o.cancelReason NOT IN :popularReasons")
    Page<Orders> findByCancelReasonNotIn(@Param("popularReasons") List<String> popularReasons, Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE "
            + "(:email IS NULL OR o.email LIKE %:email%) AND "
            + "(:telephone IS NULL OR o.telephone LIKE %:telephone%) AND "
            + "(:totalAmount IS NULL OR ABS(o.totalAmount - :totalAmount) < 0.01) AND "
            + "(:search IS NULL OR o.cancelReason LIKE %:search%)")
    Page<Orders> findByFilters(@Param("email") String email,
                                    @Param("telephone") String telephone,
                                    @Param("totalAmount") Double totalAmount,
                                    @Param("search") String search,
                                    Pageable pageable);

    @Query("SELECT o FROM Orders o WHERE " +
            "(:ShippingMethod IS NULL OR o.shippingMethod LIKE %:ShippingMethod%) AND " +
            "(:TotalAmount IS NULL OR o.totalAmount = :TotalAmount) AND " +
            "(:PaymentMethod IS NULL OR o.paymentMethod LIKE %:PaymentMethod%) AND " +
            "(:IsPaid IS NULL OR o.isPaid = :IsPaid) AND " +
            "(:Status IS NULL OR o.status = :Status) AND " +
            "(:orderCode IS NULL OR o.orderCode LIKE %:orderCode%) " +
            "ORDER BY CASE WHEN o.status = 'pending' THEN 0 ELSE 1 END, o.orderDate DESC")
    Page<Orders> filterOrders(@Param("ShippingMethod") String ShippingMethod,
                              @Param("TotalAmount") Double TotalAmount,
                              @Param("PaymentMethod") String PaymentMethod,
                              @Param("IsPaid") String IsPaid,
                              @Param("Status") String Status,
                              @Param("orderCode") String orderCode, // Thêm orderCode vào tham số
                              Pageable pageable);


    // tính tổng total amount tất cả đơn hàng có status là complete
    @Query("SELECT SUM(op.price * op.qty) " +
            "FROM Orders o " +
            "JOIN OrderProduct op ON op.order = o " +
            "WHERE o.status = 'complete' " +
            "AND op.status = 0")
    Double getTotalAmountOfCompletedOrders();

    // Lấy các đơn hàng có status là 'pending'
    @Query("SELECT o FROM Orders o WHERE o.status = 'pending'")
    Page<Orders> findPendingOrders(Pageable pageable);

    // thống kê doanh số theo năm
    @Query("SELECT MONTH(o.orderDate), SUM(op.qty) " +
            "FROM Orders o " +
            "JOIN OrderProduct op ON op.order = o " +
            "WHERE o.status = 'complete' " +
            "AND op.status = 0 " +
            "AND YEAR(o.orderDate) = :year " +
            "GROUP BY MONTH(o.orderDate) " +
            "ORDER BY MONTH(o.orderDate)")
    List<Object[]> findCompletedOrdersByYear(@Param("year") int year);

    // thống kê doanh thu theo năm
    @Query("SELECT MONTH(o.orderDate), SUM(op.price * op.qty) " +
            "FROM Orders o " +
            "JOIN OrderProduct op ON op.order = o " +
            "WHERE o.status = 'complete' " +
            "AND op.status = 0 " +
            "AND YEAR(o.orderDate) = :year " +
            "GROUP BY MONTH(o.orderDate) " +
            "ORDER BY MONTH(o.orderDate)")
    List<Object[]> findTotalRevenueByYear(@Param("year") int year);
    // thống kê biểu đò tròn tính số lượng status order
    @Query("SELECT o.status, COUNT(o) FROM Orders o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();
    Orders findFirstByOrderByIdDesc();

}
