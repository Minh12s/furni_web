package com.example.furni.service;

import com.example.furni.entity.Blog;
import com.example.furni.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Optional<Blog> getBlogById(int id) {
        return blogRepository.findById(id);
    }
    public Page<Blog> getBlogsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blogRepository.findAll(pageable);
    }
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog updateBlog(int id, Blog blogDetails) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setTitle(blogDetails.getTitle());
        blog.setTag(blogDetails.getTag());
        blog.setThumbnail(blogDetails.getThumbnail());
        blog.setContent(blogDetails.getContent());
        blog.setBlogDate(blogDetails.getBlogDate());
        return blogRepository.save(blog);
    }

    public void deleteBlog(int id) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        blogRepository.delete(blog);
    }
}
