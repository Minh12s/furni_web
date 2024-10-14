package com.example.furni.service;

import com.example.furni.entity.Favorite;
import com.example.furni.entity.Product;
import com.example.furni.entity.User;
import com.example.furni.repository.FavoriteRepository;
import com.example.furni.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    public void addToFavorite(User user, Product product) {
        Favorite favorite = new Favorite(product, user);
        favoriteRepository.save(favorite);
    }
    public boolean isFavorite(User user, Product product) {
        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product);
        return favorite != null;
    }

    public void removeFromFavorite(User user, Product product) {
        Favorite favorite = favoriteRepository.findByUserAndProduct(user, product);
        if (favorite != null) {
            favoriteRepository.delete(favorite);
        }
    }
    public int getFavoriteItemCount(Integer userId) {
        return favoriteRepository.countFavoriteByUserId(userId);
    }
    public List<Favorite> getFavoritesByUserId(int userId) {
        return favoriteRepository.findAllByUserId(userId);
    }
    public void clearFavoritesByUserId(int userId) {
        List<Favorite> favorites = favoriteRepository.findAllByUserId(userId);
        favoriteRepository.deleteAll(favorites);
    }

}

