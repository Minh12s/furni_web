package com.example.furni.controllers.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String categoryName;
    private String slug;

    public Category(int id, String categoryName, String slug) {
        this.id = id;
        this.categoryName = categoryName;
        this.slug = slug;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Category(){

    }
}
