package com.example.furni.controllers.admin;

import com.example.furni.service.DataStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class DataStatisticsController {

    @Autowired
    private DataStatisticsService dataStatisticsService;

    @GetMapping("/sales-statistics")
    public String salesStatisticsPage() {
        return "admin/SalesStatistics/salesStatistics";
    }

    @GetMapping("/RevenueChart")
    public ResponseEntity<Map<String, Object>> getSalesStatisticsData(@RequestParam(required = false) Integer year) {
        // Lấy năm hiện tại nếu không có tham số
        int selectedYear = (year != null) ? year : LocalDate.now().getYear();

        // Mảng chứa nhãn tháng
        String[] monthLabels = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

        // Lấy dữ liệu số lượng sản phẩm đã bán từ service
        int[] productsSold = dataStatisticsService.getProductsSoldByYear(selectedYear);

        // Tạo map để trả về dưới dạng JSON
        Map<String, Object> response = new HashMap<>();
        response.put("labels", monthLabels);
        response.put("productsSold", productsSold);

        // Trả về dữ liệu dưới dạng JSON
        return ResponseEntity.ok(response);
    }
    @GetMapping("/RevenueByYearChart")
    public ResponseEntity<Map<String, Object>> getRevenueByYearData(@RequestParam(required = false) Integer year) {
        int selectedYear = (year != null) ? year : LocalDate.now().getYear();

        String[] monthLabels = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
        double[] revenue = dataStatisticsService.getRevenueByYear(selectedYear);

        Map<String, Object> response = new HashMap<>();
        response.put("labels", monthLabels);
        response.put("revenue", revenue);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/OrderStatusStatistics")
    public ResponseEntity<Map<String, Integer>> getOrderStatusStatistics() {
        Map<String, Integer> statusCounts = dataStatisticsService.getOrderStatusStatistics();
        return ResponseEntity.ok(statusCounts);
    }
}
