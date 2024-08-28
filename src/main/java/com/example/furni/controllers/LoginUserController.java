    package com.example.furni.controllers;

    import com.example.furni.entity.User;
    import com.example.furni.service.UserService;  // Add this service to handle User saving logic
    import jakarta.servlet.http.HttpServletRequest;
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
        public String logout(HttpServletRequest request) {
            request.getSession().invalidate(); // Xóa session
            return "redirect:/loginPage"; // Redirect đến trang login
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

            // Gán userName bằng fullName
            user.setUserName(user.getFullName());

            userService.registerUser(user); // Gọi phương thức để lưu người dùng và vai trò

            return "redirect:/loginPage";
        }


    }
