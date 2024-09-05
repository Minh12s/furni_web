package com.example.furni.controllers.User;

import com.example.furni.entity.Blog;
import com.example.furni.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/blogUser")
public class UserBlogController extends BaseController{

    @Autowired
    private BlogService blogService;

    @GetMapping
    public String showBlogs(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {
        Page<Blog> blogsPage = blogService.getBlogsPaginatedForUser(page, size);
        List<String> tags = blogService.getAllTags();
        model.addAttribute("blogsPage", blogsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogsPage.getTotalPages());
        model.addAttribute("tags", tags);

        return "User/blog";
    }
    @GetMapping("/tag/{tag}")
    public String filterByTag(@PathVariable("tag") String tag,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model) {
        Page<Blog> blogsPage = blogService.getBlogsByTag(tag, page, size); // Get blogs by tag
        List<String> tags = blogService.getAllTags(); // Get all tags

        model.addAttribute("blogsPage", blogsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogsPage.getTotalPages());
        model.addAttribute("tags", tags); // Add tags to model

        return "User/blog";
    }

    @GetMapping("/{id}")
    public String blogDetail(@PathVariable("id") int id, Model model) {
        Blog blog = blogService.getBlogByIdForUser(id).orElseThrow(() -> new RuntimeException("Blog not found"));
        model.addAttribute("blog", blog);
        return "User/blogdetails";
    }

}
