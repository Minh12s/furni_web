package com.example.furni.controllers.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/MyOrder")
public class ProfileController {
    @GetMapping("/Profile")
    public String Profile(){
        return "MyOrder/Profile";
    }
    @GetMapping("/EditProfile")
    public String EditProfile(){
        return "MyOrder/EditProfile";
    }
}
