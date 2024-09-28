package com.example.furni.repository;

import com.example.furni.entity.OrderCancel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCancelRepository extends JpaRepository<OrderCancel, Integer> {
    @Query("SELECT o.reason, COUNT(o) FROM OrderCancel o GROUP BY o.reason")
    List<Object[]> countByReason();
    Page<OrderCancel> findAll(Pageable pageable);
    @Query("SELECT o FROM OrderCancel o WHERE "
            + "(:email IS NULL OR o.order.email LIKE %:email%) AND "
            + "(:telephone IS NULL OR o.order.telephone LIKE %:telephone%) AND "
            + "(:totalAmount IS NULL OR ABS(o.order.totalAmount - :totalAmount) < 0.01) AND "
            + "(:search IS NULL OR o.reason LIKE %:search%)")
    Page<OrderCancel> findByFilters(@Param("email") String email,
                                    @Param("telephone") String telephone,
                                    @Param("totalAmount") Double totalAmount,
                                    @Param("search") String search,
                                    Pageable pageable);

}
