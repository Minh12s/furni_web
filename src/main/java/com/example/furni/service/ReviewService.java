package com.example.furni.service;

import com.example.furni.entity.Product;
import com.example.furni.entity.Review;
import com.example.furni.repository.ProductRepository;
import com.example.furni.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Page<Product> filterProducts(String productName, Double priceFrom, Double priceTo,Double avgRating, Pageable pageable) {
        return productRepository.findProductsByCriteria(productName, priceFrom, priceTo,avgRating, pageable);
    }
    public Page<Product> getProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public Optional<Double> getAverageRatingForProduct(int productId) {
        return reviewRepository.findAverageRatingByProductId(productId);
    }
    // list review
    public Page<Review> getReviewsByProductId(int productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable);
    }

    // details review
    public Optional<Review> getReviewById(int reviewId) {
        return reviewRepository.findById(reviewId);
    }
    // Update review status
    public void updateStatus(Long reviewId, String status) {
        // Cập nhật status cho review bằng repository
        reviewRepository.updateStatus(reviewId, status);
    }
    public void save(Review review) {
        reviewRepository.save(review);
    }
    public double calculateAverageRating(int productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);

        // Lọc ra các đánh giá đã được phê duyệt
        double average = reviews.stream()
                .filter(review -> review.getStatus().equals("approved"))
                .mapToInt(Review::getRatingValue)
                .average()
                .orElse(0.0);

        // Làm tròn kết quả tới 1 chữ số thập phân
        return Math.round(average * 10.0) / 10.0;
    }



    public int countReviews(int productId) {
        return reviewRepository.countByProductId(productId);
    }
    public List<Review> getApprovedCommentsByProductId(int productId) {
        return reviewRepository.findByProductIdAndStatus(productId, "approved");
    }
}
