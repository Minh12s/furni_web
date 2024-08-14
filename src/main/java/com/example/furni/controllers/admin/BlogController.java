package com.example.furni.controllers.admin;

import com.example.furni.entity.Blog;
import com.example.furni.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public String showBlogs(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        Page<Blog> blogsPage = blogService.getBlogsPaginated(page, size);
        model.addAttribute("blogsPage", blogsPage);
        return "admin/Blog/blog";
    }

    @GetMapping("/addBlog")
    public String showAddBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "admin/Blog/addBlog";
    }

    @PostMapping("/addBlog")
    public String addBlog(@ModelAttribute("blog") Blog blog,
                          @RequestParam("thumbnailFile") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                blog.setThumbnail(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Gán giá trị ngày giờ hiện tại nếu blogDate là null
        if (blog.getBlogDate() == null) {
            blog.setBlogDate(LocalDateTime.now());
        }

        blogService.saveBlog(blog);
        return "redirect:/admin/blogs";
    }


    @GetMapping("/editBlog/{id}")
    public String showEditBlogForm(@PathVariable int id, Model model) {
        Blog blog = blogService.getBlogById(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        model.addAttribute("blog", blog);
        return "admin/Blog/editBlog";
    }

    @PostMapping("/editBlog/{id}")
    public String editBlog(@PathVariable int id,
                           @ModelAttribute("blog") Blog blogDetails,
                           @RequestParam("thumbnailFile") MultipartFile file) {
        Blog existingBlog = blogService.getBlogById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        existingBlog.setTitle(blogDetails.getTitle());
        existingBlog.setTag(blogDetails.getTag());
        existingBlog.setContent(blogDetails.getContent());

        // Xử lý hình ảnh nếu có
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                existingBlog.setThumbnail(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Không ghi đè thumbnail hiện tại nếu không có tệp mới
        blogService.updateBlog(id, existingBlog);
        return "redirect:/admin/blogs";
    }



    @PostMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable int id) {
        blogService.deleteBlog(id);
        return "redirect:/admin/blogs";
    }
}
