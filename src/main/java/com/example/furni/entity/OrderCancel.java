package com.example.furni.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "order_Cancel")
public class OrderCancel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderCancelId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Column(name = "reason")
    private String reason;

    public OrderCancel(int orderCancelId, Orders order, String reason) {
        this.orderCancelId = orderCancelId;
        this.order = order;
        this.reason = reason;
    }

    public int getOrderCancelId() {
        return orderCancelId;
    }

    public void setOrderCancelId(int orderCancelId) {
        this.orderCancelId = orderCancelId;
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
