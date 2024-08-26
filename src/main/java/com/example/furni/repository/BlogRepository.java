package com.example.furni.repository;

import com.example.furni.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    Page<Blog> findAll(Pageable pageable);
    @Query("SELECT DISTINCT b.tag FROM Blog b")
    List<String> findDistinctTags();

    Page<Blog> findByTag(String tag, Pageable pageable);
}
