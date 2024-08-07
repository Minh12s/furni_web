package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CustomerController {
    @GetMapping("/customers")
    public String Customer(){
        return "admin/Customer/customer";
    }
    @GetMapping("/orderuser")
    public String OrderUser(){
        return "admin/Customer/OrderUser";
    }
    @GetMapping("/orderdetailuser")
    public String OrderDetailsUser(){
        return "admin/Customer/OrderDetailUser";
    }
}
