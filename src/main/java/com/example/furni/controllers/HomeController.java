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

    @GetMapping("/login")
    public String Login(){
        return "User/login";
    }
    @GetMapping("/register")
    public String Register(){
        return "User/register";
    }
}