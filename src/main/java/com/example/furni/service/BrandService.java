package com.example.furni.service;

import com.example.furni.entity.Brand;
import com.example.furni.entity.Category;
import com.example.furni.entity.Material;
import com.example.furni.repository.BrandRepository;
import com.example.furni.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Page<Brand> getBrandsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return brandRepository.findAll(pageable);
    }

    public Optional<Brand> getBrandById(int id) {
        return brandRepository.findById(id);
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteBrand(int id) {
        if (productRepository.existsByBrand_Id(id)) {
            throw new IllegalStateException("The brand cannot be deleted because there are already products in the brand.");
        }

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        brandRepository.delete(brand);
    }

    public boolean isBrandNameExists(String brandName) {
        return brandRepository.existsByBrandName(brandName);
    }

    public boolean isBrandNameExists(String brandName, int excludeId) {
        return brandRepository.existsByBrandNameAndIdNot(brandName, excludeId);
    }
}
