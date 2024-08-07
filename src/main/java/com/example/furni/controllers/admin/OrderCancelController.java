package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class OrderCancelController {

    @GetMapping("/orderCancel")
    public String OrderCancel(){
        return "admin/OrderCancel/orderCancel";
    }

    @GetMapping("/orderCancelDetails")
    public String OrderCancelDetails(){
        return "admin/OrderCancel/orderCancelDetails";
    }
}
