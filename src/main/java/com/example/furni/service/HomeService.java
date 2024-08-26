package com.example.furni.service;

import com.example.furni.entity.Product;
import com.example.furni.repository.HomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeService {

    @Autowired
    private HomeRepository homeRepository;

    public List<Product> searchProducts(String searchString) {
        return homeRepository.searchByProductName(searchString);
    }
}
