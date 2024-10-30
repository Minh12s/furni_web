package com.example.furni.repository;

import com.example.furni.entity.Product;
import com.example.furni.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT AVG(r.ratingValue) FROM Review r WHERE r.product.id = :productId AND r.status = 'approved'")
    Optional<Double> findAverageRatingByProductId(@Param("productId") int productId);

    @Query("SELECT r FROM Review r WHERE r.product.id = :productId " +
            "ORDER BY CASE WHEN r.status = 'pending' THEN 0 ELSE 1 END, r.reviewDate DESC")
    Page<Review> findByProductId(@Param("productId") int productId, Pageable pageable);

    Page<Review> findByStatus(String status, Pageable pageable);
    List<Review> findByProductId(int productId);
    int countByProductIdAndStatus(int productId, String status);
    List<Review> findByProductIdAndStatus(int productId, String status);
    @Modifying
    @Transactional
    @Query("UPDATE Review r SET r.status = ?2 WHERE r.id = ?1")
    void updateStatus(Long reviewId, String status);
}
