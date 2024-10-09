package com.example.furni.repository;

import com.example.furni.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Favorite findByUserIdAndProductId(int userId, int productId);
    List<Favorite> findByUserId(int userId);
    boolean existsByUserIdAndProductId(int userId, int productId);
    void deleteByUserIdAndProductId(int userId, int productId);
    void deleteAllByUserId(int userId);
    int countFavoriteByUserId(Integer userId);

}

