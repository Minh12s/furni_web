package com.example.furni.controllers.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int materialId;

    private String materialName;

    public Material(int materialId, String materialName) {
        this.materialId = materialId;
        this.materialName = materialName;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Material(){

    }
}
