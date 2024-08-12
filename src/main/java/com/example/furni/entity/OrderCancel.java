package com.example.furni.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "order_Cancel")
public class OrderCancel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Column(name = "reason")
    private String reason;

    public OrderCancel(int id, Orders order, String reason) {
        this.id = id;
        this.order = order;
        this.reason = reason;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public OrderCancel(){
    }
}
