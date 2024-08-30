package com.example.furni.repository;

import com.example.furni.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByBrandName(String brandName);
    boolean existsByBrandNameAndIdNot(String brandName, int id);
}

