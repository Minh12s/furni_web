package com.example.furni.service;

import com.example.furni.entity.Notification;
import com.example.furni.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Lấy danh sách thông báo của user
    public List<Map<String, String>> getNotificationsByUserId(int userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return notifications.stream().map(notification -> {
            Map<String, String> map = new HashMap<>();
            map.put("title", notification.getTitle());
            map.put("message", notification.getMessage());
            map.put("createdAt", calculateTimeAgo(notification.getCreatedAt()));
            return map;
        }).collect(Collectors.toList());
    }

    // Lấy số lượng thông báo của user
    public long getNotificationCountByUserId(int userId) {
        return notificationRepository.countByUserId(userId);
    }

    // Tính thời gian kể từ khi thông báo được tạo
    private String calculateTimeAgo(LocalDateTime createdAt) {
        Duration duration = Duration.between(createdAt, LocalDateTime.now());
        if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " hours ago";
        } else {
            return duration.toDays() + " days ago";
        }
    }
}
