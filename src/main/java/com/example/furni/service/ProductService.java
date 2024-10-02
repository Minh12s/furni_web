package com.example.furni.service;

import com.example.furni.entity.Category;
import com.example.furni.entity.Product;
import com.example.furni.repository.CategoryRepository;
import com.example.furni.repository.ProductRepository;
import com.example.furni.repository.OrderProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    public Page<Product> getProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }
    public Set<String> getAllAvailableColors() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.stream()
                .filter(p -> p.getColor() != null)
                .map(Product::getColor)
                .collect(Collectors.toSet());
    }

    public Page<Product> getRelatedProducts(int categoryId, int productId, int size) {
        Pageable pageable = PageRequest.of(0, size); // Lấy trang đầu tiên với số lượng sản phẩm là `size`
        return productRepository.findByCategory_IdAndIdNot(categoryId, productId, pageable);
    }
    public Page<Product> filterProductsByCategoryWithCriteria(String slug, Double minPrice, Double maxPrice,
                                                              String sizeFilter, String color, String material, Integer brandId,
                                                              int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.filterProductsByCategoryWithCriteria(slug, minPrice, maxPrice, sizeFilter, color, material, brandId, pageable);
    }
    public Page<Product> getProductsByCategoryPaginated(String slug, int page, int size) {
        Category category = categoryRepository.findBySlug(slug);
        if (category != null) {
            return productRepository.findByCategory_Id(category.getId(), PageRequest.of(page, size));
        } else {
            return Page.empty(); // Không có category với slug tương ứng
        }
    }

    public Page<Product> filterProducts(String name, Integer categoryId, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.filterProducts(name, categoryId, minPrice, maxPrice, pageable);
    }

    public Page<Product> filterProductsWithMultipleCriteria( Double minPrice, Double maxPrice,
                                                            String size, String color, String material, Integer brandId,
                                                            int page, int sizePerPage) {
        Pageable pageable = PageRequest.of(page, sizePerPage);
        return productRepository.filterProductsWithCriteria(
                 minPrice, maxPrice, size, color, material, brandId, pageable);
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
