package com.example.furni.controllers.admin;

import com.example.furni.entity.Blog;
import com.example.furni.service.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blogs")
    public String showBlogs(Model model,
                            @RequestParam(value = "Title", required = false) String title,
                            @RequestParam(value = "Tag", required = false) String tag,
                            @RequestParam(value = "StartDate", required = false) String startDate,
                            @RequestParam(value = "EndDate", required = false) String endDate,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            HttpSession session) {
        LocalDateTime startDateTime = startDate != null && !startDate.isEmpty() ? LocalDateTime.parse(startDate + "T00:00:00") : null;
        LocalDateTime endDateTime = endDate != null && !endDate.isEmpty() ? LocalDateTime.parse(endDate + "T23:59:59") : null;

        Page<Blog> blogsPage = blogService.filterBlogs(title, tag, startDateTime, endDateTime, page, size);
        model.addAttribute("blogsPage", blogsPage);

        // Lấy thông báo thành công từ session và xóa sau khi lấy
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        return "admin/Blog/blog";
    }

    @GetMapping("/addBlog")
    public String showAddBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "admin/Blog/addBlog";
    }

    @PostMapping("/addBlog")
    public String addBlog(@ModelAttribute("blog") Blog blog,
                          @RequestParam("thumbnailFile") MultipartFile file,
                          HttpSession session) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                blog.setThumbnail(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (blog.getBlogDate() == null) {
            blog.setBlogDate(LocalDateTime.now());
        }

        blogService.saveBlog(blog);
        session.setAttribute("successMessage", "Blog added successfully!");
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
                           @RequestParam("thumbnailFile") MultipartFile file,
                           HttpSession session) {
        Blog existingBlog = blogService.getBlogById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        existingBlog.setTitle(blogDetails.getTitle());
        existingBlog.setTag(blogDetails.getTag());
        existingBlog.setContent(blogDetails.getContent());

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(bytes);
                existingBlog.setThumbnail(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        blogService.updateBlog(id, existingBlog);
        session.setAttribute("successMessage", "Blog updated successfully!");
        return "redirect:/admin/blogs";
    }

    @PostMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable int id, HttpSession session) {
        blogService.deleteBlog(id);
        session.setAttribute("successMessage", "Blog deleted successfully!");
        return "redirect:/admin/blogs";
    }
}
