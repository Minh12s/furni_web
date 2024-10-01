package com.example.furni.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "slug")
    private String slug;
    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image;

    // Constructor không tham số (bắt buộc)
    public Category() {
    }

    // Constructor với các tham số

    public Category(int id, String categoryName, String image, String slug) {
        this.id = id;
        this.categoryName = categoryName;
        this.slug = slug;
        this.image = image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
