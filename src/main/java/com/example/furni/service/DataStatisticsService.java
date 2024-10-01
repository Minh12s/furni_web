package com.example.furni.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.furni.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataStatisticsService {
    @Autowired
    private OrdersRepository ordersRepository;

    public int[] getProductsSoldByYear(int year) {
        // Mảng chứa số sản phẩm bán ra trong từng tháng
        int[] productsSold = new int[12];

        // Lấy dữ liệu từ repository
        List<Object[]> results = ordersRepository.findCompletedOrdersByYear(year);

        // Duyệt qua dữ liệu và gán số sản phẩm bán ra vào đúng tháng
        for (Object[] result : results) {
            int month = (int) result[0]; // Tháng
            long productsSoldInMonth = (long) result[1]; // Tổng số sản phẩm bán ra trong tháng đó
            productsSold[month - 1] = (int) productsSoldInMonth;
        }

        return productsSold;
    }
    public double[] getRevenueByYear(int year) {
        // Khởi tạo mảng doanh thu cho 12 tháng
        double[] revenue = new double[12];

        // Lấy dữ liệu doanh thu theo từng tháng trong năm từ repository
        List<Object[]> results = ordersRepository.findTotalRevenueByYear(year);

        // Duyệt qua kết quả và lưu doanh thu vào mảng, với chỉ số là tháng (1 -> 12)
        for (Object[] result : results) {
            int month = (int) result[0];  // Tháng
            double totalAmount = (double) result[1];
            revenue[month - 1] = totalAmount;
        }

        return revenue;
    }
    public Map<LocalDateTime, Integer> getProductsSoldByDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> results = ordersRepository.findProductsSoldByDate(startDateTime, endDateTime);
        Map<LocalDateTime, Integer> productsSoldMap = new HashMap<>();

        for (Object[] result : results) {
            LocalDateTime dateTime = (LocalDateTime) result[0];
            long quantitySold = (long) result[1];
            productsSoldMap.put(dateTime, (int) quantitySold);
        }

        return productsSoldMap;
    }

    public Map<LocalDateTime, Double> getRevenueByDate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<Object[]> results = ordersRepository.findRevenueByDate(startDateTime, endDateTime);
        Map<LocalDateTime, Double> revenueMap = new HashMap<>();

        for (Object[] result : results) {
            LocalDateTime dateTime = (LocalDateTime) result[0];
            double totalAmount = (double) result[1];
            revenueMap.put(dateTime, totalAmount);
        }

        return revenueMap;
    }
    public Map<String, Integer> getOrderStatusStatistics() {
        List<Object[]> results = ordersRepository.countOrdersByStatus();
        Map<String, Integer> statusCounts = new HashMap<>();

        for (Object[] result : results) {
            String status = (String) result[0];
            Long count = (Long) result[1];
            statusCounts.put(status, count.intValue());
        }

        return statusCounts;
    }
}
