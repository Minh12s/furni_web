package com.example.furni.controllers.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime orderDate;
    private double totalAmount;
    private String status;
    private String isPaid;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private String fullName;
    private String email;
    private String telephone;
    private String paymentMethod;
    private String shippingMethod;
    private String note;
    private LocalDateTime schedule;
}
