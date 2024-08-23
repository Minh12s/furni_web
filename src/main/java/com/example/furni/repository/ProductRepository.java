package com.example.furni.repository;

import com.example.furni.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySlug(String slug);
}
