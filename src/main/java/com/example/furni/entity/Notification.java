package com.example.furni.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // ID thông báo

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Ràng buộc với bảng user
    private User user; // Người dùng nhận thông báo

    @ManyToOne
    @JoinColumn(name = "order_id") // Ràng buộc với bảng orders (nếu có)
    private Orders order; // Đơn hàng liên quan
    @Column(nullable = false, length = 255)
    private String title; // Tiêu đề thông báo
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message; // Nội dung chi tiết của thông báo

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Thời gian tạo thông báo

    // Getters và Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

