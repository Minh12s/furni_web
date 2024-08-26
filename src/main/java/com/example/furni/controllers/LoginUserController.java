package com.example.furni.controllers;

import com.example.furni.entity.User;
import com.example.furni.service.UserService;  // Add this service to handle User saving logic
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginUserController {
    @Autowired
    private UserService userService;  // Inject UserService

    @GetMapping("/loginPage")
    public String loginPage() {
        return "User/login";
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/loginPage"; // Redirect đến trang login sau khi đăng xuất
    }

    @GetMapping("/registers")
    public String registerPage(Model model) {
        model.addAttribute("user", new User()); // Initialize a new User object for the form
        return "User/register";
    }

    @PostMapping("/registers")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "User/register";
        }
        // Gán userName mặc định là "user" nếu chưa được gán
        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            user.setUserName("user");
        }

        userService.registerUser(user); // Gọi phương thức để lưu người dùng và vai trò

        return "redirect:/loginPage";
    }


}
