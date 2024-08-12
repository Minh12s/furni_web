package com.example.furni.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_product")
@IdClass(OrderProductId.class)
public class OrderProduct {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "price")
    private double price;

    @Column(name = "qty")
    private int qty;

    @Column(name = "status")
    private int status;

    // Constructors, getters, and setters
    public OrderProduct() {}

    public OrderProduct(Orders order, Product product, double price, int qty, int status) {
        this.order = order;
        this.product = product;
        this.price = price;
        this.qty = qty;
        this.status = status;
    }

    // Getters and Setters

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
