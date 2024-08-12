package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class BrandController {
    @GetMapping("/brand")
    public String Category(){
        return "admin/brand/brand";
    }
    @GetMapping("/addBrand")
    public String AddCategory(){
        return "/admin/brand/addBrand";
    }
    @GetMapping("/editBrand")
    public String EditCategory(){
        return "/admin/brand/editBrand";
    }
}
