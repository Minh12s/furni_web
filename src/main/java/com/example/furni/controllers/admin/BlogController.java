package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class BlogController {
    @GetMapping("/blogs")
    public String Blog(){
        return "admin/Blog/blog";
    }

    @GetMapping("/addBlog")
    public String addBlog(){
        return "admin/Blog/addBlog";
    }

    @GetMapping("/editBlog")
    public String editBlog(){
        return "admin/Blog/editBlog";
    }
}
