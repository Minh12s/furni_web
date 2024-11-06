package com.example.furni.repository;

import com.example.furni.entity.OrderReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderReturnRepository extends JpaRepository<OrderReturn, Integer> {
    Page<OrderReturn> findByUserId(int userId, Pageable pageable);

    @Query("SELECT o.reason, COUNT(o) FROM OrderReturn o GROUP BY o.reason")
    List<Object[]> countByReason();
    @Query("SELECT o FROM OrderReturn o WHERE "
            + "(:status IS NULL OR o.status = :status) AND "
            + "(:refundAmount IS NULL OR o.refundAmount = :refundAmount) AND "
            + "(:search IS NULL OR o.reason LIKE %:search%)")
    Page<OrderReturn> findByFilters(@Param("status") String status,
                                    @Param("refundAmount") Double refundAmount,
                                    @Param("search") String search,
                                    Pageable pageable);

}
