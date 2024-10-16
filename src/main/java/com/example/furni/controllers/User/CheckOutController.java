package com.example.furni.controllers.User;

import com.example.furni.entity.Cart;
import com.example.furni.entity.Orders;
import com.example.furni.entity.OrderProduct;
import com.example.furni.entity.Product;
import com.example.furni.entity.User;
import com.example.furni.service.CartService;
import com.example.furni.service.OrderService;
import com.example.furni.service.ProductService;
import com.example.furni.service.UserService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/")
public class CheckOutController extends BaseController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    private double getShippingFee(String shippingMethod) {
        switch (shippingMethod) {
            case "J&T Express":
                return 10.00; // Phí vận chuyển cho J&T Express
            case "Ninja Van":
                return 5.00; // Phí vận chuyển cho Ninja Van
            default:
                return 0.00; // Phí vận chuyển mặc định
        }
    }

    @GetMapping("/checkouts")
    public String checkout(HttpServletRequest request, Model model) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        // Lấy các sản phẩm trong giỏ hàng của người dùng
        List<Cart> cartItems = cartService.getCartItemsByUserId(userId);

        // Tính tổng tiền
        double subtotal = cartItems.stream().mapToDouble(Cart::getTotal).sum();
        double tax = subtotal * 0.10; // Ví dụ thuế 10%
        double shippingFee = 0.00;   // Phí vận chuyển mặc định
        double totalAmount = subtotal + tax + shippingFee;

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("totalAmount", totalAmount);

        return "User/checkout"; // Trả về trang checkout
    }


    @PostMapping("/checkout")
    public String processCheckout(
            @RequestParam("FullName") String fullName,
            @RequestParam("Email") String email,
            @RequestParam("AddressDetail") String addressDetail,
            @RequestParam("ProvinceName") String provinceName,
            @RequestParam("DistrictName") String districtName,
            @RequestParam("WardName") String wardName,
            @RequestParam("Telephone") String telephone,
            @RequestParam("ShippingMethod") String shippingMethod,
            @RequestParam("PaymentMethod") String paymentMethod,
            @RequestParam("Note") String note,
            @RequestParam("Schedule") String schedule,
            HttpServletRequest request,
            Model model) {

        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<Cart> cartItems = cartService.getCartItemsByUserId(userId);

        if (cartItems.isEmpty()) {
            model.addAttribute("errorMessage", "Giỏ hàng của bạn đang trống.");
            return "User/checkout";
        }

        double subtotal = cartItems.stream().mapToDouble(Cart::getTotal).sum();
        double tax = subtotal * 0.10;
        double shippingFee = getShippingFee(shippingMethod);
        double totalAmount = subtotal + tax + shippingFee;

        Orders order = new Orders();
        User user = userService.findById(userId);
        order.setUser(user);
        order.setFullName(fullName);
        order.setEmail(email);
        order.setAddressDetail(addressDetail);
        order.setProvince(provinceName);
        order.setDistrict(districtName);
        order.setWard(wardName);
        order.setTelephone(telephone);
        order.setShippingMethod(shippingMethod);
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(LocalDateTime.now());
        order.setNote(note);
        LocalDateTime scheduleDateTime = LocalDateTime.parse(schedule);
        order.setSchedule(scheduleDateTime);
        order.setStatus("pending");
        // Sinh mã đơn hàng ngẫu nhiên
        String orderCode = generateOrderCode();
        order.setOrderCode(orderCode);

        // Lưu đơn hàng vào cơ sở dữ liệu nhưng không đánh dấu là đã thanh toán ngay
        order.setIsPaid("0"); // Chưa thanh toán
        order.setTotalAmount(totalAmount);
        orderService.saveOrder(order);



// Kiểm tra phương thức thanh toán là PayPal
        if (paymentMethod.equals("PayPal")) {
            // Tạo đối tượng Payer và Payment cho PayPal như đoạn mã ban đầu
            Payer payer = new Payer();
            payer.setPaymentMethod("paypal");

            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);

            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(String.valueOf(totalAmount));

            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setDescription("Thanh toán đơn hàng từ giỏ hàng.");

            List<Transaction> transactions = new ArrayList<>();
            transactions.add(transaction);

            payment.setTransactions(transactions);

            // Thiết lập URL redirect khi thành công và hủy
            RedirectUrls redirectUrls = new RedirectUrls();
            redirectUrls.setCancelUrl("http://localhost:8080/payment/cancel");
            redirectUrls.setReturnUrl("http://localhost:8080/payment/success");
            payment.setRedirectUrls(redirectUrls);

            try {
                // Tạo thanh toán
                Payment createdPayment = payment.create(getApiContext());

                // Lấy link chuyển hướng
                String approvalLink = createdPayment.getLinks().stream()
                        .filter(link -> "approval_url".equals(link.getRel()))
                        .findFirst()
                        .orElseThrow(() -> new Exception("Approval link not found"))
                        .getHref();

                // Chuyển hướng đến PayPal
                return "redirect:" + approvalLink;
            } catch (PayPalRESTException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Có lỗi xảy ra trong quá trình thanh toán.");
                return "User/checkout";
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "Có lỗi xảy ra trong việc lấy liên kết thanh toán.");
                return "User/checkout";
            }
        }

        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQty();

            OrderProduct orderProduct = new OrderProduct(order, product, product.getPrice(), quantity, 0);
            orderService.saveOrderProduct(orderProduct);

            int updatedQuantity = product.getQty() - quantity;
            product.setQty(updatedQuantity);
            productService.save(product);
        }

        cartService.clearCartByUserId(userId);

        orderService.sendThankYouEmail(fullName, email, order, cartItems,tax,subtotal,shippingFee, totalAmount);

        return "redirect:/thankyou";
    }
    private String generateOrderCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder orderCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) { // Mã đơn hàng 8 ký tự
            int index = random.nextInt(characters.length());
            orderCode.append(characters.charAt(index));
        }
        return orderCode.toString();
    }

    // Phương thức lấy APIContext
    private APIContext getApiContext() {
        String clientId = "AWvcIeV4fzJa8pqpjlXS9hwtUS1a0iQK1j_FG9nB1YQXxvwRC00KkDIgaEVpKJqNDZqSubCmmvVyNoMq"; // Thay đổi với client ID của bạn
        String clientSecret = "EOwtiAoKEfbS5sB_kVe3LjD2Ubnngj8P-iYEoIX5ms03dxQqdJk3EL1CQa9bHHG08L3BCgxQFCUCGshZ"; // Thay đổi với client secret của bạn
        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox"); // Hoặc "live" nếu bạn đã chuyển sang môi trường thực
        return apiContext;
    }
    // Đường dẫn cho thanh toán thành công
    @GetMapping("/payment/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId,
                                 Model model, HttpServletRequest request) {
        try {
            // Tạo đối tượng PaymentExecution để xác nhận thanh toán
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            // Tìm kiếm payment bằng paymentId
            Payment payment = new Payment();
            payment.setId(paymentId);

            // Xác nhận thanh toán
            Payment executedPayment = payment.execute(getApiContext(), paymentExecution);

            // Kiểm tra xem thanh toán có thành công hay không
            if (executedPayment.getState().equals("approved")) {
                // Lấy đơn hàng mới nhất
                Orders order = orderService.getLatestOrder();

                if (order != null) {
                    // Cập nhật trạng thái đơn hàng thành đã thanh toán
                    order.setIsPaid("1"); // Đã thanh toán
                    orderService.saveOrder(order); // Lưu lại đơn hàng

                    // Xử lý giỏ hàng và trừ số lượng sản phẩm
                    Integer userId = (Integer) request.getSession().getAttribute("userId");
                    List<Cart> cartItems = cartService.getCartItemsByUserId(userId);

                    // Trừ số lượng sản phẩm trong kho
                    for (Cart cartItem : cartItems) {
                        Product product = cartItem.getProduct();
                        int quantity = cartItem.getQty();

                        // Lưu từng sản phẩm vào order_product
                        OrderProduct orderProduct = new OrderProduct(order, product, product.getPrice(), quantity, 0);
                        orderService.saveOrderProduct(orderProduct);

                        // Cập nhật số lượng sản phẩm còn lại trong kho
                        int updatedQuantity = product.getQty() - quantity;
                        product.setQty(updatedQuantity);
                        productService.save(product);
                    }

                    // Xóa giỏ hàng
                    cartService.clearCartByUserId(userId);

                    // Tính toán các khoản tiền cho email
                    double subtotal = cartItems.stream().mapToDouble(Cart::getTotal).sum();
                    double tax = subtotal * 0.10;
                    double shippingFee = getShippingFee(order.getShippingMethod());
                    double totalAmount = subtotal + tax + shippingFee;

                    // Gửi email xác nhận
                    orderService.sendThankYouEmail(order.getFullName(), order.getEmail(), order, cartItems, tax, subtotal, shippingFee, totalAmount);

                    return "redirect:/thankyou"; // Chuyển hướng đến trang cảm ơn
                } else {
                    // Xử lý khi đơn hàng không tồn tại
                    model.addAttribute("errorMessage", "Không tìm thấy đơn hàng.");
                    return "redirect:/error";
                }
            }

        } catch (PayPalRESTException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Có lỗi xảy ra khi xác nhận thanh toán.");
            return "User/checkout";
        }

        return "redirect:/checkout"; // Nếu thanh toán không thành công
    }



    // Đường dẫn cho thanh toán bị hủy
    @GetMapping("/payment/cancel")
    public String paymentCancel(Model model) {
        model.addAttribute("errorMessage", "Bạn đã hủy thanh toán.");
        return "User/checkout"; // Quay lại trang checkout
    }


}