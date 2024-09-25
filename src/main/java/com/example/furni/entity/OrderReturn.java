package com.example.furni.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
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
    @OneToMany(mappedBy = "orderReturn", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReturnImages> returnImages;


    public List<ReturnImages> getReturnImages() {
        return returnImages;
    }

    public void setReturnImages(List<ReturnImages> returnImages) {
        this.returnImages = returnImages;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public OrderReturn(int id, Orders order, User user, Product product, LocalDateTime returnDate, String status, String reason, double refundAmount, String description, List<ReturnImages> returnImages) {
        this.id = id;
        this.order = order;
        this.user = user;
        this.product = product;
        this.returnDate = returnDate;
        this.status = status;
        this.reason = reason;
        this.refundAmount = refundAmount;
        this.description = description;
        this.returnImages = returnImages;
    }

    public OrderReturn(){
    }
}
