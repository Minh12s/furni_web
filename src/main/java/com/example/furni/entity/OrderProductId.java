package com.example.furni.entity;

import java.io.Serializable;
import java.util.Objects;

public class OrderProductId implements Serializable {
    private int order;
    private int product;

    // Constructors, getters, setters, equals, and hashCode

    public OrderProductId() {}

    public OrderProductId(int order, int product) {
        this.order = order;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProductId that = (OrderProductId) o;
        return order == that.order && product == that.product;
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, product);
    }
}
