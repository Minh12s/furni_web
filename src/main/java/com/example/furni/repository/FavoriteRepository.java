package com.example.furni.repository;

import com.example.furni.entity.Favorite;
import com.example.furni.entity.Product;
import com.example.furni.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    int countFavoriteByUserId(Integer userId);
    Favorite findByUserAndProduct(User user, Product product);
    List<Favorite> findAllByUserId(int userId);


}

