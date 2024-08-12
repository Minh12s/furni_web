package com.example.furni.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "brandName")
    private String brandName;

    // Constructor không tham số (bắt buộc)
    public Brand() {
    }

    // Constructor với các tham số
    public Brand(String brandName) {
        this.brandName = brandName;
    }

    // Getters và setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
