package com.example.furni.controllers.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Return_Images")
public class ReturnImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    @ManyToOne
    @JoinColumn(name = "returnId")
    private OrderReturn orderReturn;

    private String imagePath;

    public ReturnImages(int imageId, OrderReturn orderReturn, String imagePath) {
        this.imageId = imageId;
        this.orderReturn = orderReturn;
        this.imagePath = imagePath;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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
