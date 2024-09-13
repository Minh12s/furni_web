package com.example.furni.repository;

import com.example.furni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);

    // Kiểm tra email đã tồn tại
    boolean existsByEmail(String email);

    // Kiểm tra full name đã tồn tại
    boolean existsByFullName(String fullName);
}
