package com.example.furni.controllers.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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
    @GetMapping("/loginAdmin")
    public String login(){
        return "admin/login";
    }
    @GetMapping("/logoutAdmin")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/admin/loginAdmin";
    }
}
