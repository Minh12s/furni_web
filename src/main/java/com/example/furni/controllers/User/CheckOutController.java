package com.example.furni.controllers.User;

import com.example.furni.entity.Cart;
import com.example.furni.entity.Orders;
import com.example.furni.entity.Product;
import com.example.furni.entity.User;
import com.example.furni.service.CartService;
import com.example.furni.service.OrderService;
import com.example.furni.service.ProductService;
import com.example.furni.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

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

        // Lấy userId từ session
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
        double shippingFee = 10.00;
        double totalAmount = subtotal + tax + shippingFee;

        // Tạo đối tượng Orders và thiết lập thông tin
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
        order.setIsPaid("0");
        order.setTotalAmount(totalAmount);

        // Lưu đơn hàng
        orderService.saveOrder(order);

        // Cập nhật số lượng sản phẩm
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct(); // Lấy Product từ Cart
            int quantity = cartItem.getQty();   // Lấy số lượng từ giỏ hàng

            // Trừ số lượng sản phẩm
            int updatedQuantity = product.getQty() - quantity;
            product.setQty(updatedQuantity);

            // Cập nhật thông tin sản phẩm trong cơ sở dữ liệu
            productService.save(product);
        }


        // Xóa giỏ hàng sau khi đặt hàng thành công
        cartService.clearCartByUserId(userId);

        return "redirect:/thankyou";
    }

}
