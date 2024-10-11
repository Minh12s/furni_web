package com.example.furni.entity;

import java.io.Serializable;
import java.util.Objects;

public class FavoriteId implements Serializable {

    private Long product;
    private Long user;

    public FavoriteId() {
    }

    public FavoriteId(Long product, Long user) {
        this.product = product;
        this.user = user;
    }

    // Phương thức equals và hashCode để xác định composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(product, that.product) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, user);
    }

    // Getters và setters nếu cần
}
