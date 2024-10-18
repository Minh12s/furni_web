package com.example.furni.controllers;

import com.example.furni.controllers.User.BaseController;
import com.example.furni.entity.*;
import com.example.furni.service.*;
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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/MyOrder")
public class MyOrderController extends BaseController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private myOrderService myOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;
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
        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
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

        String successMessage = (String) request.getSession().getAttribute("reviewMessage");
        if (successMessage != null) {
            model.addAttribute("reviewMessage", successMessage);
            request.getSession().removeAttribute("reviewMessage"); // Remove the message after retrieving
        }

        return "MyOrder/MyOrder"; // Trả về tên view để hiển thị
    }
    @PostMapping("/updateStatus")
    public String updateStatus(
            @RequestParam("id") int id,
            @RequestParam("status") String status,
            @RequestParam("cancel_reason") String cancelReason,
            @RequestParam(value = "other_reason", required = false) String otherReason,
            HttpSession session) {

        Orders order = orderService.getOrderById(id);
        if (order != null) {
            // Nếu người dùng chọn "Other" và nhập lý do cụ thể, sử dụng lý do từ otherReason thay vì "Other"
            if ("Other".equals(cancelReason) && otherReason != null && !otherReason.trim().isEmpty()) {
                cancelReason = otherReason; // Thay thế "Other" bằng lý do thực sự
            }

            // Cập nhật trạng thái và lý do hủy đơn hàng
            order.setStatus(status);
            order.setCancelReason(cancelReason);
            orderService.saveOrder(order);

            // Thêm thông báo thành công vào session
            session.setAttribute("reviewMessage", "Order cancel updated successfully!");
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
        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
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
        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
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
        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
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
        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
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
        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
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
        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
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
    public String OrderReturn(Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
        }
        return "MyOrder/OrderReturn";
    }
    @GetMapping("/OrderDetail/{id}")
    public String OrderDetail(@PathVariable("id") int id, Model model , HttpSession session){
        Orders order = orderService.getOrderById(id);
        if (order != null) {
            List<OrderProduct> orderProducts = orderService.getOrderProductsByOrderId(id);
            model.addAttribute("order", order);
            model.addAttribute("orderProducts", orderProducts);
        }
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
        }
        return "MyOrder/OrderDetail";
    }
    @GetMapping("/Review/{productId}")
    public String showReviewPage(@PathVariable int productId, Model model) {
        model.addAttribute("productId", productId);
        return "MyOrder/Review"; // Trả về trang review
    }


    @PostMapping("/Review/{productId}")
    public String submitReview(
            @PathVariable int productId,
            @RequestParam("RatingValue") int ratingValue,
            @RequestParam("Comment") String comment,
            HttpServletRequest request) {

        // Lấy userId từ session
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        // Lấy thông tin user và product
        User user = userService.findById(userId);  // Sử dụng userId để tìm user
        Product product = productService.findById(productId);  // Tìm product bằng productId

        // Tạo đối tượng Review
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setComment(comment);
        review.setRatingValue(ratingValue);
        review.setReviewDate(LocalDateTime.now());
        review.setStatus("pending"); // Trạng thái ban đầu là pending

        // Lưu review vào cơ sở dữ liệu
        reviewService.save(review);
        request.getSession().setAttribute("reviewMessage", "Your review has been successfully .");

        return "redirect:/MyOrder/MyOrder"; // Redirect về trang MyOrder sau khi review thành công
    }
    @GetMapping("/RequestRefund")
    public String RequestRefund(Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
        }
        return "MyOrder/RequestRefund";
    }
    @GetMapping("/ReasonCancel/{id}")
    public String ReasonCancel(@PathVariable("id") int id, Model model, HttpSession session){
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
        }
        return "MyOrder/ReasonCancel";
    }

}
