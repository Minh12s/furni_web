package com.example.furni.repository;

import com.example.furni.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findByCategory_Id(int categoryId, Pageable pageable);
    Page<Product> findByCategory_IdAndIdNot(int categoryId, int productId, Pageable pageable);

    Product findBySlug(String slug);
    @Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.productName LIKE %:name%) " +
            "AND (:categoryId IS NULL OR p.category.id = :categoryId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> filterProducts(@Param("name") String name,
                                 @Param("categoryId") Integer categoryId,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice,
                                 Pageable pageable);
    @Query("SELECT p FROM Product p "
            + "LEFT JOIN Review r ON p.id = r.product.id "
            + "WHERE (:productName IS NULL OR p.productName LIKE %:productName%) "
            + "AND (:priceFrom IS NULL OR p.price >= :priceFrom) "
            + "AND (:priceTo IS NULL OR p.price <= :priceTo) "
            + "GROUP BY p "
            + "HAVING (:avgRating IS NULL OR AVG(r.ratingValue) = :avgRating)")
    Page<Product> findProductsByCriteria(@Param("productName") String productName,
                                         @Param("priceFrom") Double priceFrom,
                                         @Param("priceTo") Double priceTo,
                                         @Param("avgRating") Double avgRating,
                                         Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            " (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:size IS NULL OR p.size.sizeName = :size) " +
            "AND (:color IS NULL OR p.color = :color) " +
            "AND (:material IS NULL OR p.material.materialName = :material) " +
            "AND (:brandId IS NULL OR p.brand.id = :brandId)")
    Page<Product> filterProductsWithCriteria(
                                             @Param("minPrice") Double minPrice,
                                             @Param("maxPrice") Double maxPrice,
                                             @Param("size") String size,
                                             @Param("color") String color,
                                             @Param("material") String material,
                                             @Param("brandId") Integer brandId,
                                             Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:slug IS NULL OR p.category.slug = :slug) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:sizeFilter IS NULL OR p.size.sizeName = :sizeFilter) AND " +
            "(:color IS NULL OR p.color = :color) AND " +
            "(:material IS NULL OR p.material.materialName = :material) AND " +
            "(:brandId IS NULL OR p.brand.id = :brandId)")
    Page<Product> filterProductsByCategoryWithCriteria(@Param("slug") String slug,
                                                       @Param("minPrice") Double minPrice,
                                                       @Param("maxPrice") Double maxPrice,
                                                       @Param("sizeFilter") String sizeFilter,
                                                       @Param("color") String color,
                                                       @Param("material") String material,
                                                       @Param("brandId") Integer brandId,
                                                       Pageable pageable);

    boolean existsByProductName(String productName);
    boolean existsByProductNameAndIdNot(String productName, int id);
    // Đếm tổng số sản phẩm
    long count();
    // Phương thức để tìm các sản phẩm hết hàng
    Page<Product> findByQty(int qty, Pageable pageable);
}

