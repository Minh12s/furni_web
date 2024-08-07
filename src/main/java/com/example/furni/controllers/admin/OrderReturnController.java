package com.example.furni.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class OrderReturnController {

    @GetMapping("/orderReturn")
    public String OrderReturn(){
        return "admin/OrderReturn/orderReturn";
    }
    @GetMapping("/orderReturnDetails")
    public String OrderReturnDetails(){
        return "admin/OrderReturn/orderReturnDetails";
    }

    @GetMapping("/detailsProductReturn")
    public String DetailsProductReturn(){
        return "admin/OrderReturn/detailsProductReturn";
    }
    @GetMapping("/rejectReason")
    public String RejectReason(){
        return "admin/OrderReturn/rejectReason";
    }

}
