package com.example.furni.controllers.User;

import com.example.furni.entity.Cart;
import com.example.furni.entity.Product;
import com.example.furni.service.CartService;
import com.example.furni.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class CartController extends BaseController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add-to-cart")
    public String addToCart(HttpServletRequest request,
                            @RequestParam int productId,
                            @RequestParam int qty,
                            @RequestParam(required = false) String slug,
                            Model model) {

        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            model.addAttribute("error", "You need to login to add products to cart.");
            return "User/login";
        }

        Product product = productService.findById(productId);
        if (product == null) {
            model.addAttribute("error", "Product does not exist.");
            return "User/details";
        }


        if (qty > product.getQty()) {
            request.getSession().setAttribute("errorDetailMessage", "The quantity requested exceeds the quantity available in stock.");
            return "redirect:/product/details/" + (slug != null ? slug : product.getSlug());
        }

        double total = product.getPrice() * qty;
        cartService.addToCart(userId, product, qty, total);  // Gọi phương thức đã cập nhật để thêm sản phẩm và cộng dồn

        request.getSession().setAttribute("successMessage", "Product added successfully.");

        return "redirect:/product/details/" + (slug != null ? slug : product.getSlug());
    }

    // Hiển thị giỏ hàng
    @GetMapping("/carts")
    public String showCart(HttpServletRequest request, Model model, HttpSession session) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<Cart> cartItems = cartService.getCartItemsByUserId(userId);
        model.addAttribute("cartItems", cartItems);

        Set<Integer> outOfStockProductIds = new HashSet<>(); // Sử dụng Set để lưu trữ ID sản phẩm hết hàng

        // Kiểm tra tình trạng tồn kho
        for (Cart item : cartItems) {
            Product product = productService.findById(item.getProduct().getId());
            if (product == null || item.getQty() > product.getQty()) {
                outOfStockProductIds.add(item.getProduct().getId()); // Thêm ID vào Set
            }
        }

        model.addAttribute("outOfStockProductIds", outOfStockProductIds); // Thêm vào model


        double subtotal = cartItems.stream().mapToDouble(Cart::getTotal).sum();
//        double delivery = 5.0; // phí giao hàng mặc định
        double total = subtotal ;

        model.addAttribute("subtotal", subtotal);
//        model.addAttribute("delivery", delivery);
        model.addAttribute("total", total);

        double totalPrice = cartItems.stream().mapToDouble(Cart::getTotal).sum();
        model.addAttribute("totalPrice", totalPrice);


        // Get the success message from session and remove it after retrieval
        String successMessage = (String) session.getAttribute("cartMessage");
        if (successMessage != null) {
            model.addAttribute("cartMessage", successMessage);
            session.removeAttribute("cartMessage");
        }

        return "User/cart";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/remove-from-cart")
    public String removeFromCart(HttpServletRequest request,
                                 @RequestParam int cartId,
                                 Model model) {

        cartService.removeFromCart(cartId);
        request.getSession().setAttribute("cartMessage", "The product has been removed from the cart.");

        return "redirect:/carts";
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/update-cart")
    public String updateCart(HttpServletRequest request,
                             @RequestParam int cartId,
                             @RequestParam int qty,
                             Model model) {
        Cart cartItem = cartService.findById(cartId);
        int maxQty = cartItem.getProduct().getQty();

        Product product = cartItem.getProduct();
        if (qty > maxQty) {
            model.addAttribute("errorMessage", "Sorry, you can only purchase a maximum of " + product.getQty() + " of this product.");
            model.addAttribute("cartItems", cartService.getCartItemsByUserId((Integer) request.getSession().getAttribute("userId")));
            return "redirect:/carts";
        }

        cartService.updateCartQuantityById(cartId, qty);
        request.getSession().setAttribute("cartMessage", "Product quantity has been updated.");
        return "redirect:/carts";
    }

    // Xóa toàn bộ sản phẩm trong giỏ hàng
    @PostMapping("/clear-cart")
    public String clearCart(HttpServletRequest request, Model model) {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId != null) {
            cartService.clearCart(userId);
            request.getSession().setAttribute("cartMessage", "Cart has been cleared.");
        }

        return "redirect:/carts";
    }
}
