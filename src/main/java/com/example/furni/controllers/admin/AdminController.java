package com.example.furni.controllers.admin;

import com.example.furni.entity.Orders;
import com.example.furni.entity.Review;
import com.example.furni.service.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String Dashboard(@RequestParam(value = "orderPage", defaultValue = "0") int orderPage,
                            @RequestParam(value = "reviewPage", defaultValue = "0") int reviewPage,
                            @RequestParam(value = "size", defaultValue = "1") int size,
                            Model model) {
        long totalUsers = dashboardService.getTotalUsers();
        double totalOrderAmount = dashboardService.getTotalOrderAmount();
        long totalProducts = dashboardService.getTotalProducts();

        Page<Orders> pendingOrdersPage = dashboardService.getPendingOrders(orderPage, size);
        Page<Review> pendingReviewsPage = dashboardService.getPendingReviews(reviewPage, size);

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalOrderAmount", totalOrderAmount);
        model.addAttribute("totalProducts", totalProducts);

        model.addAttribute("pendingOrders", pendingOrdersPage.getContent());
        model.addAttribute("pendingReviews", pendingReviewsPage.getContent());

        model.addAttribute("totalOrderPages", pendingOrdersPage.getTotalPages());
        model.addAttribute("totalReviewPages", pendingReviewsPage.getTotalPages());
        model.addAttribute("currentOrderPage", orderPage);
        model.addAttribute("currentReviewPage", reviewPage);

        return "admin/dashboard";
    }

}
