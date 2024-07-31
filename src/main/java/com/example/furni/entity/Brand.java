package com.example.furni.controllers.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int brandId;

    private String brandName;

    public Brand(int brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Brand (){

    }
}
