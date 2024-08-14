package com.example.furni.repository;

import com.example.furni.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {
    Page<Size> findAll(Pageable pageable);
}
