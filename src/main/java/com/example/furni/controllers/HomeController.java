package com.example.furni.controllers;

import org.springframework.ui.Model;
import com.example.furni.entity.Product;
import com.example.furni.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private HomeService homeService;
    @GetMapping
    public String Home(){
        return "User/index";
    }
    @GetMapping("/details")
    public String Detail(){
        return "User/details";
    }
    @GetMapping("/cart")
    public String Cart(){
        return "User/cart";
    }
    @GetMapping("/about")
    public String About(){
        return "User/about";
    }
    @GetMapping("/checkout")
    public String Checkout(){
        return "User/checkout";
    }
    @GetMapping("/thankyou")
    public String Thankyou(){
        return "User/thankyou";
    }
    @GetMapping("/favorite")
    public String Favorite(){
        return "User/favorite";
    }

    @GetMapping("/search")
    public String search(@RequestParam("searchString") String searchString, Model model) {
        List<Product> products = homeService.searchProducts(searchString);
        model.addAttribute("products", products);
        return "User/search";
    }

}