package com.example.furni.repository;

import com.example.furni.entity.ReturnImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnImagesRepository extends JpaRepository<ReturnImages, Integer> {
    List<ReturnImages> findByOrderReturnId(int orderReturnId);
}
