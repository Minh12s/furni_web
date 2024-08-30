package com.example.furni.repository;

import com.example.furni.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findAll(Pageable pageable);
    @Query("SELECT DISTINCT b.tag FROM Blog b")
    List<String> findDistinctTags();

    Page<Blog> findByTag(String tag, Pageable pageable);
    @Query("SELECT b FROM Blog b WHERE (:title IS NULL OR b.title LIKE %:title%) " +
            "AND (:tag IS NULL OR b.tag LIKE %:tag%) " +
            "AND (:startDate IS NULL OR b.blogDate >= :startDate) " +
            "AND (:endDate IS NULL OR b.blogDate <= :endDate)")
    Page<Blog> filterBlogs(@Param("title") String title,
                           @Param("tag") String tag,
                           @Param("startDate") LocalDateTime startDate,
                           @Param("endDate") LocalDateTime endDate,
                           Pageable pageable);
    boolean existsByTitle(String title);
    boolean existsByTitleAndIdNot(String title, int id);
}
