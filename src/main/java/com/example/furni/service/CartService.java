package com.example.furni.service;

import com.example.furni.entity.Cart;
import com.example.furni.entity.Product;
import com.example.furni.entity.User;
import com.example.furni.repository.CartRepository;
import com.example.furni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    // Thêm sản phẩm vào giỏ hàng
    // Thêm sản phẩm vào giỏ hàng
    public Cart addToCart(int userId, Product product, int qty, double total) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại");
        }

        // Kiểm tra nếu sản phẩm đã có trong giỏ hàng
        Cart existingCartItem = cartRepository.findByUserIdAndProductId(userId, product.getId());

        if (existingCartItem != null) {
            // Cộng dồn số lượng sản phẩm đã tồn tại trong giỏ hàng
            int newQty = existingCartItem.getQty() + qty;
            existingCartItem.setQty(newQty);
            existingCartItem.setTotal(existingCartItem.getProduct().getPrice() * newQty);
            return cartRepository.save(existingCartItem);
        } else {
            // Thêm sản phẩm mới vào giỏ hàng
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setQty(qty);
            cart.setTotal(total);
            return cartRepository.save(cart);
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeFromCart(int cartId) {
        cartRepository.deleteById(cartId);
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public void updateCartQuantity(int userId, int productId, int qty) {
        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, productId);
        if (cartItem != null) {
            cartItem.setQty(qty);
            cartItem.setTotal(cartItem.getProduct().getPrice() * qty);
            cartRepository.save(cartItem);
        }
    }

    // Cập nhật số lượng sản phẩm bằng cartId
    public void updateCartQuantityById(int cartId, int qty) {
        Cart cartItem = cartRepository.findById(cartId).orElse(null);
        if (cartItem != null) {
            cartItem.setQty(qty);
            cartItem.setTotal(cartItem.getProduct().getPrice() * qty);
            cartRepository.save(cartItem);
        }
    }

    // Xóa toàn bộ sản phẩm trong giỏ hàng
    public void clearCart(int userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartItems);
    }

    public List<Cart> getCartItemsByUserId(int userId) {
        return cartRepository.findByUserId(userId);
    }

    public boolean isProductInCart(int userId, int productId) {
        return cartRepository.existsByUserIdAndProductId(userId, productId);
    }

    public int getCartItemCount(Integer userId) {
        return cartRepository.countByUserId(userId);
    }
}
