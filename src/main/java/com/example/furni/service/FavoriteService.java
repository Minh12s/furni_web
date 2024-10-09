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

    public void addToFavorite(int userId, Product product) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User không tồn tại");
        }

        // Kiểm tra nếu sản phẩm đã có trong danh sách yêu thích
        Favorite existingFavorite = favoriteRepository.findByUserIdAndProductId(userId, product.getId());

        if (existingFavorite != null) {
            // Nếu sản phẩm đã tồn tại, không thêm lại
            throw new IllegalArgumentException("Product already in favorites");
        } else {
            // Thêm sản phẩm mới vào danh sách yêu thích
            Favorite favorite = new Favorite();
            favorite.setUser(user);
            favorite.setProduct(product);
            favorite.setProductName(product.getProductName());
            favorite.setThumbnail(product.getThumbnail());
            favorite.setPrice(product.getPrice());

            favoriteRepository.save(favorite);
        }
    }
    @Transactional
    public boolean isProductInFavorites(int userId, int productId) {
        return favoriteRepository.existsByUserIdAndProductId(userId, productId);
    }
    @Transactional
    public void removeFavoriteByProductId(int productId, int userId) {
        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }

    public List<Favorite> findFavoritesByUserId(int userId) {
        return favoriteRepository.findByUserId(userId);
    }
    public void removeFavoriteById(int favoriteId, int userId) {
        favoriteRepository.deleteById(favoriteId);
    }
    @Transactional
    public void clearFavoritesByUserId(int userId) {
        favoriteRepository.deleteAllByUserId(userId);
    }
    public int getFavoriteItemCount(Integer userId) {
        return favoriteRepository.countFavoriteByUserId(userId);
    }
}

