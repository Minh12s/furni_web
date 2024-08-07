package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class OrderController {
    @GetMapping("/orders")
    public String Order(){
        return "admin/Order/order";
    }
    @GetMapping("/detailorder")
    public String DetailOrder(){
        return "admin/Order/DetailOrder";
    }
}

