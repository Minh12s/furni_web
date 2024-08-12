package com.example.furni.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "role")
    private String role;

    // Constructor không tham số (bắt buộc)
    public Role() {
    }

    // Constructor với các tham số
    public Role(User user, String role) {
        this.user = user;
        this.role = role;
    }

    // Getters và setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
