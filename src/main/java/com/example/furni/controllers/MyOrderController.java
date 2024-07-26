package com.example.furni.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/MyOrder")
public class MyOrderController {

    @GetMapping("/MyOrder")
    public String MyOrder(){
        return "MyOrder/MyOrder";
    }
    @GetMapping("/ChangePassword")
    public String ChangePassword(){
        return "MyOrder/ChangePassword";
    }
    @GetMapping("/Profile")
    public String Profile(){
        return "MyOrder/Profile";
    }
    @GetMapping("/EditProfile")
    public String EditProfile(){
        return "MyOrder/EditProfile";
    }

}
