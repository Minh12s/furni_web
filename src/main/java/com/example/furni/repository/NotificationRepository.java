package com.example.furni.repository;

import com.example.furni.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(int userId);

    // Đếm số lượng thông báo của người dùng
    long countByUserId(int userId);
}
