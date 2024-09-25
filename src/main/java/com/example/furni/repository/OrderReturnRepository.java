package com.example.furni.repository;

import com.example.furni.entity.OrderReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderReturnRepository extends JpaRepository<OrderReturn, Integer> {
    @Query("SELECT o.reason, COUNT(o) FROM OrderReturn o GROUP BY o.reason")
    List<Object[]> countByReason();
}
