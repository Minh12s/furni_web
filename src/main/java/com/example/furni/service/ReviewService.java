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
    public boolean updateReviewStatus(int reviewId, String status) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            if ("pending".equals(review.getStatus())) {
                review.setStatus(status);  // Update status to "approved" or "rejected"
                reviewRepository.save(review);
                return true;
            }
        }
        return false;
    }
}
