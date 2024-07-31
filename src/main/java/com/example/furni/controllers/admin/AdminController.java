package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String Dashboard(){
        return "admin/dashboard";
    }
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
    @GetMapping("/customer")
    public String Customer(){
        return "admin/Customer/customer";
    }
    @GetMapping("/order")
    public String Order(){
        return "admin/Order/order";
    }
    @GetMapping("/product")
    public String Product(){
        return "admin/Product/product";
    }
    @GetMapping("/blog")
    public String Blog(){
        return "admin/Blog/blog";
    }

    @GetMapping("/login")
    public String Login(){
        return "admin/login";
    }
}
