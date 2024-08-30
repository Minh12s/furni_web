package com.example.furni.service;

import com.example.furni.entity.Blog;
import com.example.furni.entity.Brand;
import com.example.furni.entity.Category;
import com.example.furni.entity.Product;
import com.example.furni.repository.CategoryRepository;
import com.example.furni.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }
    public Page<Product> getProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }
    public Page<Product> getProductsByCategoryPaginated(String slug, int page, int size) {
        Category category = categoryRepository.findBySlug(slug);
        if (category != null) {
            // Tìm sản phẩm dựa trên category_id
            return productRepository.findByCategory_Id(category.getId(), PageRequest.of(page, size));
        } else {
            return Page.empty(); // Không có category với slug tương ứng
        }
    }
    public Page<Product> filterProducts(String name, Integer categoryId, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.filterProducts(name, categoryId, minPrice, maxPrice, pageable);
    }
    public Product findById(int id) {
        return productRepository.findById(id).orElse(null);
    }
    public Product findBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }

    public boolean isProductNameExists(String productName) {
        return productRepository.existsByProductName(productName);
    }

    public boolean isProductNameExists(String productName, int excludeId) {
        return productRepository.existsByProductNameAndIdNot(productName, excludeId);
    }

}
