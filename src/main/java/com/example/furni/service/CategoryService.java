package com.example.furni.service;

import com.example.furni.entity.Category;
import com.example.furni.repository.CategoryRepository;
import com.example.furni.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    public Page<Category> getCategoriesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(int id, Category categoryDetails) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryName(categoryDetails.getCategoryName());
        category.setSlug(categoryDetails.getSlug());
        return categoryRepository.save(category);
    }

    public void deleteCategory(int id) {
        // Kiểm tra xem có sản phẩm nào liên kết với category không
        if (productRepository.existsByCategory_Id(id)) {
            throw new IllegalStateException("The category cannot be deleted because there are already products in the category.");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }
    public boolean isCategoryNameExists(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }

    public boolean isCategoryNameExists(String categoryName, int excludeId) {
        return categoryRepository.existsByCategoryNameAndIdNot(categoryName, excludeId);
    }
}
