package com.example.furni.controllers.admin;

import com.example.furni.entity.Material;
import com.example.furni.entity.OrderProduct;
import com.example.furni.entity.Orders;
import com.example.furni.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Trang danh sách đơn hàng
    @GetMapping("/orders")
    public String Order(Model model, HttpSession session,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) String ShippingMethod,
                        @RequestParam(required = false) Double TotalAmount,
                        @RequestParam(required = false) String PaymentMethod,
                        @RequestParam(required = false) String IsPaid,
                        @RequestParam(required = false) String Status,
                        @RequestParam(required = false) String orderCode) {
        // Gọi đến service để lấy danh sách đơn hàng đã được lọc
        Page<Orders> ordersPage = orderService.filterOrders(page, size, ShippingMethod, TotalAmount, PaymentMethod, IsPaid, Status, orderCode);

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("size", size);
        model.addAttribute("statusClass", new HashMap<String, String>() {{
            put("pending", "text-pending");
            put("confirmed", "text-confirmed");
            put("shipping", "text-shipping");
            put("shipped", "text-shipped");
            put("complete", "text-complete");
            put("cancel", "text-cancel");
        }});

        // Lấy thông báo thành công từ session và xóa sau khi lấy
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        return "admin/Order/order";
    }


    // Trang chi tiết đơn hàng
    @GetMapping("/detailorder/{id}")
    public String DetailOrder(@PathVariable("id") int id, Model model) {
        Orders order = orderService.getOrderById(id);
        if (order != null) {
            List<OrderProduct> orderProducts = orderService.getOrderProductsByOrderId(id);
            model.addAttribute("order", order);
            model.addAttribute("orderProducts", orderProducts);
        }
        return "admin/Order/DetailOrder";
    }
    // Cập nhật trạng thái đơn hàng
    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("id") int id, @RequestParam("status") String status, HttpSession session) {
        Orders order = orderService.getOrderById(id);
        if (order != null) {
            // Kiểm tra và cập nhật trạng thái mới
            order.setStatus(status);
            orderService.saveOrder(order);
            String title = "Your order has been "+ status;
            String message;
            if ("cancel".equalsIgnoreCase(status)) {
                message = "Hello, we regret to inform you that your order #" + order.getOrderCode() + " has been canceled. If you have any questions, please contact our support team.";
            } else {
                message = "Hello, your order #" + order.getOrderCode() + " has been " + status + ". Thank you for shopping at our store!";
            }
            orderService.saveNotification(order.getUser(), order,title, message);

            // Thêm thông báo thành công vào session
            session.setAttribute("successMessage", "Order status updated successfully!");
        }
        return "redirect:/admin/orders";
    }

}
