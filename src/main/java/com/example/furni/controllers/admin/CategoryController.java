package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @GetMapping("/category")
    public String Category(){
        return "admin/Category/category";
    }
    @GetMapping("/addCategory")
    public String AddCategory(){
        return "/admin/Category/addCategory";
    }
    @GetMapping("/editCategory")
    public String EditCategory(){
        return "/admin/Category/editCategory";
    }
}
