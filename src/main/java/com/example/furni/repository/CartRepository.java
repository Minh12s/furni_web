package com.example.furni.repository;

import com.example.furni.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUserId(int userId);
    boolean existsByUserIdAndProductId(int userId, int productId);
    int countByUserId(Integer userId);
    // Thêm phương thức tìm kiếm theo userId và productId
    Cart findByUserIdAndProductId(int userId, int productId);
    void deleteByUserId(Integer userId);

}
