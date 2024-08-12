package com.example.furni.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "material")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "material_name")
    private String materialName;

    // Constructor không tham số (bắt buộc)
    public Material() {
    }

    // Constructor với các tham số
    public Material(String materialName) {
        this.materialName = materialName;
    }

    // Getters và setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
