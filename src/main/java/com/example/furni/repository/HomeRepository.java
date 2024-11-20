package com.example.furni.repository;

import com.example.furni.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.productName LIKE %:searchString% AND p.deletedAt IS NULL")
    List<Product> searchByProductName(@Param("searchString") String searchString);

}
