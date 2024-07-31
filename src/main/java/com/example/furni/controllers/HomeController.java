package com.example.furni.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String Home(){
        return "User/index";
    }
    @GetMapping("/category")
    public String Category(){
        return "User/category";
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
    @GetMapping("/blog")
    public String Blog(){
        return "User/blog";
    }
    @GetMapping("/blogdetails")
    public String BlogDetail(){
        return "User/blogdetails";
    }
    @GetMapping("/contact")
    public String Contact(){
        return "User/contact";
    }
    @GetMapping("/checkout")
    public String Checkout(){
        return "User/checkout";
    }
    @GetMapping("/thankyou")
    public String Thankyou(){
        return "User/thankyou";
    }

    @GetMapping("/login")
    public String Login(){
        return "User/login";
    }
    @GetMapping("/register")
    public String Register(){
        return "User/register";
    }
}