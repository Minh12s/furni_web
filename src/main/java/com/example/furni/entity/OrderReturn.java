package com.example.furni.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_return")
public class OrderReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "return_date")
    private LocalDateTime returnDate;
    @Column(name = "status")
    private String status;
    @Column(name = "reason")
    private String reason;
    @Column(name = "return_amount")
    private double refundAmount;
    @Column(name = "description")
    private String description;

    public OrderReturn(int id, Orders order, User user, LocalDateTime returnDate, String status, String reason, double refundAmount, String description) {
        this.id = id;
        this.order = order;
        this.user = user;
        this.returnDate = returnDate;
        this.status = status;
        this.reason = reason;
        this.refundAmount = refundAmount;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderReturn(){
    }
}
