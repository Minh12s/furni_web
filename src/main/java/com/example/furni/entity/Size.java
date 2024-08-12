package com.example.furni.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "size")
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sizeName")
    private String sizeName;

    // Constructor không tham số (bắt buộc)
    public Size() {
    }

    // Constructor với các tham số
    public Size(String sizeName) {
        this.sizeName = sizeName;
    }

    // Getters và setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }
}
