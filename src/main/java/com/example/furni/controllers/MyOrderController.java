package com.example.furni.controllers;

import com.example.furni.controllers.User.BaseController;
import com.example.furni.entity.OrderProduct;
import com.example.furni.entity.Orders;
import com.example.furni.service.CartService;
import com.example.furni.service.OrderService;
import com.example.furni.service.myOrderService;
import com.example.furni.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/MyOrder")
public class MyOrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private myOrderService myOrderService;
    @GetMapping("/MyOrder")
    public String myOrder(
            HttpServletRequest request,
            Model model,
            @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
            @RequestParam(defaultValue = "9") int size, // Kích thước mặc định là 9
            @RequestParam(defaultValue = "id,desc") String sort // Sắp xếp mặc định theo id giảm dần
    ) {
        // Lấy userId từ session
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        // Kiểm tra userId để tránh lỗi NullPointerException
        if (userId == null) {
            return "redirect:/login"; // Điều hướng đến trang login nếu user chưa đăng nhập
        }

        // Tạo Pageable với các tham số phân trang và sắp xếp
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Lấy danh sách đơn hàng bằng userId với phân trang
        Page<Orders> ordersPage = orderService.findOderByUserId(userId, pageable);

        // Thêm danh sách đơn hàng và thông tin phân trang vào model để hiển thị trên view
        model.addAttribute("orders", ordersPage.getContent()); // Danh sách đơn hàng
        model.addAttribute("currentPage", ordersPage.getNumber()); // Trang hiện tại
        model.addAttribute("totalPages", ordersPage.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", size); // Kích thước trang

        return "MyOrder/MyOrder"; // Trả về tên view để hiển thị
    }
    @PostMapping("/updateStatus")
    public String updateStatus(@RequestParam("id") int id, @RequestParam("status") String status, HttpSession session) {
        Orders order = orderService.getOrderById(id);
        if (order != null) {
            // Kiểm tra và cập nhật trạng thái mới
            order.setStatus(status);
            orderService.saveOrder(order);

            // Thêm thông báo thành công vào session
            session.setAttribute("successMessage", "Order status updated successfully!");
        }
        return "redirect:/MyOrder/MyOrder";
    }


    @GetMapping("/OrderCancel")
    public String OrderCancel(HttpServletRequest request,
                              Model model,
                              @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
                              @RequestParam(defaultValue = "1") int size, // Kích thước mặc định là 9
                              @RequestParam(defaultValue = "id,desc") String sort // Sắp xếp mặc định theo id giảm dần
    ) {
        // Lấy userId từ session
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        // Kiểm tra userId để tránh lỗi NullPointerException
        if (userId == null) {
            return "redirect:/login"; // Điều hướng đến trang login nếu user chưa đăng nhập
        }

        // Tạo Pageable với các tham số phân trang và sắp xếp
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Lấy danh sách đơn hàng trạng thái pending bằng userId với phân trang
        Page<Orders> ordersPage = myOrderService.findOrdersByUserIdAndStatus(userId, "cancel", pageable);

        // Thêm danh sách đơn hàng và thông tin phân trang vào model để hiển thị trên view
        model.addAttribute("orders", ordersPage.getContent()); // Danh sách đơn hàng
        model.addAttribute("currentPage", ordersPage.getNumber()); // Trang hiện tại
        model.addAttribute("totalPages", ordersPage.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", size); // Kích thước trang
        return "MyOrder/OrderCancel";
    }
    @GetMapping("/OrderPending")
    public String OrderPending(
            HttpServletRequest request,
            Model model,
            @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
            @RequestParam(defaultValue = "1") int size, // Kích thước mặc định là 9
            @RequestParam(defaultValue = "id,desc") String sort // Sắp xếp mặc định theo id giảm dần
    ) {
        // Lấy userId từ session
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        // Kiểm tra userId để tránh lỗi NullPointerException
        if (userId == null) {
            return "redirect:/login"; // Điều hướng đến trang login nếu user chưa đăng nhập
        }

        // Tạo Pageable với các tham số phân trang và sắp xếp
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Lấy danh sách đơn hàng trạng thái pending bằng userId với phân trang
        Page<Orders> ordersPage = myOrderService.findOrdersByUserIdAndStatus(userId, "pending", pageable);

        // Thêm danh sách đơn hàng và thông tin phân trang vào model để hiển thị trên view
        model.addAttribute("orders", ordersPage.getContent()); // Danh sách đơn hàng
        model.addAttribute("currentPage", ordersPage.getNumber()); // Trang hiện tại
        model.addAttribute("totalPages", ordersPage.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", size); // Kích thước trang

        return "MyOrder/OrderPending"; // Trả về tên view để hiển thị
    }

    @GetMapping("/OrderConfirmed")
    public String OrderConfirmed(
            HttpServletRequest request,
            Model model,
            @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
            @RequestParam(defaultValue = "1") int size, // Kích thước mặc định là 9
            @RequestParam(defaultValue = "id,desc") String sort // Sắp xếp mặc định theo id giảm dần
    ) {
        // Lấy userId từ session
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        // Kiểm tra userId để tránh lỗi NullPointerException
        if (userId == null) {
            return "redirect:/login"; // Điều hướng đến trang login nếu user chưa đăng nhập
        }

        // Tạo Pageable với các tham số phân trang và sắp xếp
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Lấy danh sách đơn hàng trạng thái pending bằng userId với phân trang
        Page<Orders> ordersPage = myOrderService.findOrdersByUserIdAndStatus(userId, "confirm", pageable);

        // Thêm danh sách đơn hàng và thông tin phân trang vào model để hiển thị trên view
        model.addAttribute("orders", ordersPage.getContent()); // Danh sách đơn hàng
        model.addAttribute("currentPage", ordersPage.getNumber()); // Trang hiện tại
        model.addAttribute("totalPages", ordersPage.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", size); // Kích thước trang
        return "MyOrder/OrderConfirmed";
    }
    @GetMapping("/OrderShipping")
    public String OrderShipping( HttpServletRequest request,
                                 Model model,
                                 @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
                                 @RequestParam(defaultValue = "1") int size, // Kích thước mặc định là 9
                                 @RequestParam(defaultValue = "id,desc") String sort // Sắp xếp mặc định theo id giảm dần
    ) {
        // Lấy userId từ session
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        // Kiểm tra userId để tránh lỗi NullPointerException
        if (userId == null) {
            return "redirect:/login"; // Điều hướng đến trang login nếu user chưa đăng nhập
        }

        // Tạo Pageable với các tham số phân trang và sắp xếp
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Lấy danh sách đơn hàng trạng thái pending bằng userId với phân trang
        Page<Orders> ordersPage = myOrderService.findOrdersByUserIdAndStatus(userId, "shipping", pageable);

        // Thêm danh sách đơn hàng và thông tin phân trang vào model để hiển thị trên view
        model.addAttribute("orders", ordersPage.getContent()); // Danh sách đơn hàng
        model.addAttribute("currentPage", ordersPage.getNumber()); // Trang hiện tại
        model.addAttribute("totalPages", ordersPage.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", size); // Kích thước trang

        return "MyOrder/OrderShipping";
    }
    @GetMapping("/OrderShipped")
    public String OrderShipped( HttpServletRequest request,
                                Model model,
                                @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
                                @RequestParam(defaultValue = "1") int size, // Kích thước mặc định là 9
                                @RequestParam(defaultValue = "id,desc") String sort // Sắp xếp mặc định theo id giảm dần
    ) {
        // Lấy userId từ session
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        // Kiểm tra userId để tránh lỗi NullPointerException
        if (userId == null) {
            return "redirect:/login"; // Điều hướng đến trang login nếu user chưa đăng nhập
        }

        // Tạo Pageable với các tham số phân trang và sắp xếp
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Lấy danh sách đơn hàng trạng thái pending bằng userId với phân trang
        Page<Orders> ordersPage = myOrderService.findOrdersByUserIdAndStatus(userId, "Shipped", pageable);

        // Thêm danh sách đơn hàng và thông tin phân trang vào model để hiển thị trên view
        model.addAttribute("orders", ordersPage.getContent()); // Danh sách đơn hàng
        model.addAttribute("currentPage", ordersPage.getNumber()); // Trang hiện tại
        model.addAttribute("totalPages", ordersPage.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", size); // Kích thước trang

        return "MyOrder/OrderShipped";
    }
    @GetMapping("/OrderComplete")
    public String OrderComplete( HttpServletRequest request,
                                 Model model,
                                 @RequestParam(defaultValue = "0") int page, // Trang mặc định là 0
                                 @RequestParam(defaultValue = "1") int size, // Kích thước mặc định là 9
                                 @RequestParam(defaultValue = "id,desc") String sort // Sắp xếp mặc định theo id giảm dần
    ) {
        // Lấy userId từ session
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        // Kiểm tra userId để tránh lỗi NullPointerException
        if (userId == null) {
            return "redirect:/login"; // Điều hướng đến trang login nếu user chưa đăng nhập
        }

        // Tạo Pageable với các tham số phân trang và sắp xếp
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        // Lấy danh sách đơn hàng trạng thái pending bằng userId với phân trang
        Page<Orders> ordersPage = myOrderService.findOrdersByUserIdAndStatus(userId, "complete", pageable);

        // Thêm danh sách đơn hàng và thông tin phân trang vào model để hiển thị trên view
        model.addAttribute("orders", ordersPage.getContent()); // Danh sách đơn hàng
        model.addAttribute("currentPage", ordersPage.getNumber()); // Trang hiện tại
        model.addAttribute("totalPages", ordersPage.getTotalPages()); // Tổng số trang
        model.addAttribute("pageSize", size); // Kích thước trang

        return "MyOrder/OrderComplete";
    }
    @GetMapping("/OrderReturn")
    public String OrderReturn(){
        return "MyOrder/OrderReturn";
    }
    @GetMapping("/OrderDetail/{id}")
    public String OrderDetail(@PathVariable("id") int id, Model model){
        Orders order = orderService.getOrderById(id);
        if (order != null) {
            List<OrderProduct> orderProducts = orderService.getOrderProductsByOrderId(id);
            model.addAttribute("order", order);
            model.addAttribute("orderProducts", orderProducts);
        }
        return "MyOrder/OrderDetail";
    }
    @GetMapping("/Review")
    public String Review(){
        return "MyOrder/Review";
    }
    @GetMapping("/RequestRefund")
    public String RequestRefund(){
        return "MyOrder/RequestRefund";
    }
    @GetMapping("/ReasonCancel")
    public String ReasonCancel(){
        return "MyOrder/ReasonCancel";
    }

}
