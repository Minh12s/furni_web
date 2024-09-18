package com.example.furni.repository;

import com.example.furni.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    boolean existsByProductName(String productName);
    boolean existsByProductNameAndIdNot(String productName, int id);
    // Đếm tổng số sản phẩm
    long count();
}

