package com.example.furni.controllers.admin;

import com.example.furni.entity.Material;
import com.example.furni.entity.Review;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.furni.entity.Product;
import com.example.furni.service.ReviewService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    public String review(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         @RequestParam(required = false) String productName,
                         @RequestParam(required = false) Double priceFrom,
                         @RequestParam(required = false) Double priceTo,
                         @RequestParam(required = false) Double avgRating,
                         HttpSession session) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = reviewService.filterProducts(productName, priceFrom, priceTo,avgRating, pageable);

        productsPage.getContent().forEach(product -> {
            Optional<Double> averageRating = reviewService.getAverageRatingForProduct(product.getId());
            product.setAverageRating(averageRating.orElse(0.0)); // Set số sao trung bình
        });
        // Lấy thông báo thành công từ session và xóa sau khi lấy
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("size", size);
        return "admin/Review/review";
    }
    @GetMapping("/listReview/{productId}")
    public String ListReview(
            @PathVariable int productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpSession session
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewsPage = reviewService.getReviewsByProductId(productId, pageable);

        model.addAttribute("reviewsPage", reviewsPage);

        // Lấy thông báo thành công từ session và xóa sau khi lấy
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        return "admin/Review/ListReview";
    }
    @PostMapping("/updateReviewStatus")
    public String updateReviewStatus(@RequestParam("id") Long reviewId, @RequestParam("status") String status, HttpSession session) {
        // Gọi service để update status cho review
        reviewService.updateStatus(reviewId, status);

        session.setAttribute("successMessage", "Successfully updated status review!");
        return "redirect:/admin/reviews";
    }

}
