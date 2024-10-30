package com.example.furni.controllers;

import com.example.furni.controllers.User.BaseController;
import com.example.furni.entity.*;
import com.example.furni.repository.OrderProductRepository;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private OrderReturnService orderReturnService;
    @Autowired
    private OrderProductRepository orderProductRepository;
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

        String successMessage = (String) request.getSession().getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            request.getSession().removeAttribute("successMessage"); // Remove the message after retrieving
        }

        return "MyOrder/MyOrder"; // Trả về tên view để hiển thị
    }
    @PostMapping("/updateStatus")
    public String updateStatus(
            @RequestParam("secureToken") String secureToken,
            @RequestParam("status") String status,
            @RequestParam("cancel_reason") String cancelReason,
            @RequestParam(value = "other_reason", required = false) String otherReason,
            HttpSession session) {

        Orders order = orderService.getOrderBySecureToken(secureToken);
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
    @PostMapping("/updateStatusComplete")
    public String updateStatusComplete(@RequestParam("secureToken") String secureToken,
                                       @RequestParam("status") String status,
                                       HttpServletRequest request) {
        // Lấy đơn hàng theo ID
        Orders order = myOrderService.getOrderBySecureToken(secureToken);

        if (order != null) {
            // Cập nhật trạng thái
            order.setStatus(status);
            myOrderService.saveOrder(order);

            // Lưu thông báo thành công vào session sử dụng request
            request.getSession().setAttribute("successMessage", "Order status updated successfully.");
        }
        // Chuyển hướng về trang quản lý đơn hàng của người dùng
        return "redirect:/MyOrder/MyOrder";
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
    @GetMapping("/OrderDetail/{secureToken}")
    public String OrderDetail(@PathVariable("secureToken") String secureToken, Model model , HttpSession session){
        // Tìm đơn hàng theo secureToken
        Orders order = orderService.getOrderBySecureToken(secureToken);
        if (order != null) {
            List<OrderProduct> orderProducts = orderService.getOrderProductsByOrderId(order.getId());
            model.addAttribute("order", order);
            model.addAttribute("orderProducts", orderProducts);
        } else {
            // Xử lý trường hợp không tìm thấy đơn hàng
            return "redirect:/error"; // Ví dụ, chuyển hướng đến trang lỗi
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

    @GetMapping("/Review/{slug}")
    public String showReviewPage(@PathVariable String slug, Model model, HttpServletRequest request) {
        Product product = productService.findBySlug(slug);  // Find product by slug
        model.addAttribute("product", product);

        // Retrieve and clear error message from session
        String errorMessage = (String) request.getSession().getAttribute("ErrorReviewMessage");
        if (errorMessage != null) {
            model.addAttribute("ErrorReviewMessage", errorMessage);
            request.getSession().removeAttribute("ErrorReviewMessage");
        }
        return "MyOrder/Review"; // Return the review page
    }

    @PostMapping("/Review/{slug}")
    public String submitReview(
            @PathVariable String slug,
            @RequestParam(value = "RatingValue", required = false, defaultValue = "0") int ratingValue,
            @RequestParam("Comment") String comment,
            HttpServletRequest request) {

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        User user = userService.findById(userId);  // Get user by ID
        Product product = productService.findBySlug(slug);  // Find product by slug

        if (ratingValue < 1 || ratingValue > 5) {
            request.getSession().setAttribute("ErrorReviewMessage", "Please select a valid rating.");
            return "redirect:/MyOrder/Review/" + slug;
        }

        String sanitizedComment = Jsoup.clean(comment, Safelist.none());
        if (!comment.equals(sanitizedComment)) {
            request.getSession().setAttribute("ErrorReviewMessage", "Your review is invalid. Please try again.");
            return "redirect:/MyOrder/Review/" + slug;
        }

        int MAX_COMMENT_LENGTH = 255;
        if (sanitizedComment.length() > MAX_COMMENT_LENGTH) {
            request.getSession().setAttribute("ErrorReviewMessage", "Your review is too long. Maximum length is " + MAX_COMMENT_LENGTH + " characters.");
            return "redirect:/MyOrder/Review/" + slug;
        }

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setComment(sanitizedComment);
        review.setRatingValue(ratingValue);
        review.setReviewDate(LocalDateTime.now());
        review.setStatus("pending");

        reviewService.save(review);
        request.getSession().setAttribute("reviewMessage", "Your review has been successfully submitted.");

        return "redirect:/MyOrder/MyOrder";  // Redirect to MyOrder page after successful review
    }

    @GetMapping("/RequestRefund")
    public String requestRefundForm(@RequestParam("productId") int productId,
                                    @RequestParam("orderId") int orderId,
                                    Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login";
        }

        // Lấy thông tin sản phẩm từ đơn hàng
        OrderProduct orderProduct = orderProductRepository.findByOrder_IdAndProduct_Id(orderId, productId);
        double refundAmount = orderProduct.getPrice() * orderProduct.getQty(); // Tính tổng tiền của sản phẩm

        // Gửi thông tin qty về form
        int qty = orderProduct.getQty(); // Lấy số lượng sản phẩm trong đơn hàng

        model.addAttribute("orderId", orderId); // Gửi orderId đến form
        model.addAttribute("productId", productId); // Gửi productId đến form
        model.addAttribute("refundAmount", refundAmount); // Gửi tổng tiền của sản phẩm đến form
        model.addAttribute("qty", qty); // Gửi số lượng của sản phẩm đến form

        return "MyOrder/RequestRefund";
    }



    @PostMapping("/RequestRefund")
    public String handleRefundRequest(@RequestParam("OrderId") int orderId,
                                      @RequestParam("ProductId") int productId,
                                      @RequestParam("Reason") String reason,
                                      @RequestParam("Description") String description,
                                      @RequestParam("RefundAmount") double refundAmount,
                                      @RequestParam("Qty") int qty, // Thêm trường qty
                                      @RequestParam("ImagePath") MultipartFile[] imageFiles,
                                      HttpSession session ,  HttpServletRequest request) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.findById(userId);
        Orders order = orderService.getOrderById(orderId);
        Product product = productService.findById(productId);

        OrderReturn orderReturn = new OrderReturn();
        orderReturn.setOrder(order);
        orderReturn.setUser(user);
        orderReturn.setProduct(product);
        orderReturn.setReturnDate(LocalDateTime.now());
        orderReturn.setStatus("pending"); // Đặt trạng thái mặc định là "Pending"
        orderReturn.setReason(reason);
        orderReturn.setQty(qty); // Lưu số lượng sản phẩm trả về
        orderReturn.setRefundAmount(refundAmount);
        orderReturn.setDescription(description);

        // Lưu hình ảnh dưới dạng base64 vào bảng ReturnImages
        List<ReturnImages> returnImagesList = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            if (!imageFile.isEmpty()) {
                try {
                    String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
                    ReturnImages returnImage = new ReturnImages();
                    returnImage.setImagePath(base64Image);
                    returnImage.setOrderReturn(orderReturn);
                    returnImagesList.add(returnImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        orderReturn.setReturnImages(returnImagesList);

        // Lưu thông tin vào cơ sở dữ liệu
        orderReturnService.save(orderReturn);
        request.getSession().setAttribute("successMessage", "Refund request sent successfully.");
        return "redirect:/MyOrder/MyOrder"; // Chuyển hướng về trang đơn hàng của tôi
    }


    @GetMapping("/ReasonCancel/{secureToken}")
    public String ReasonCancel(@PathVariable("secureToken") String secureToken, Model model, HttpSession session) {
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