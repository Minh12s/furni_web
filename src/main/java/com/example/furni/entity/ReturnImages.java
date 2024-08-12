package com.example.furni.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "return_images")
public class ReturnImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_return_id ")
    private OrderReturn orderReturn;
    @Column(name = "image_path")
    private String imagePath;

    public ReturnImages(int id, OrderReturn orderReturn, String imagePath) {
        this.id = id;
        this.orderReturn = orderReturn;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderReturn getOrderReturn() {
        return orderReturn;
    }

    public void setOrderReturn(OrderReturn orderReturn) {
        this.orderReturn = orderReturn;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ReturnImages(){
    }
}
