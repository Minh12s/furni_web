package com.example.furni.repository;

import com.example.furni.entity.Product;
import com.example.furni.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT AVG(r.ratingValue) FROM Review r WHERE r.product.id = :productId")
    Optional<Double> findAverageRatingByProductId(@Param("productId") int productId);

    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    List<Review> findReviewsByProductId(@Param("productId") int productId);

    Page<Review> findByProductId(int productId, Pageable pageable);
}
