package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ProductController {
    @GetMapping("/products")
    public String Product(){
        return "admin/Product/product";
    }


    @GetMapping("/addProduct")
    public String addProduct(){
        return "admin/Product/addProduct";
    }

    @GetMapping("/editProduct")
    public String editProduct(){
        return "admin/Product/editProduct";
    }
}

