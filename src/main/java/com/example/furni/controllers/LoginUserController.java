        package com.example.furni.controllers;
    
        import com.example.furni.controllers.User.BaseController;
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
        import org.springframework.web.bind.annotation.RequestParam;
        import jakarta.validation.Valid;  // Import valid annotation


        @Controller
        public class LoginUserController extends BaseController {
            @Autowired
            private UserService userService;  // Inject UserService
    
            @GetMapping("/loginPage")
            public String loginPage(Model model, @RequestParam(value = "error", required = false) String error) {
                if (error != null) {
                    // Khi có lỗi, hiển thị thông báo lỗi
                    model.addAttribute("errorLogin", "Invalid email or password. Please try again.");
                }
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
            public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
                if (result.hasErrors()) {
                    return "User/register";  // Nếu có lỗi, trả về trang đăng ký cùng với lỗi
                }

                // Gán userName bằng fullName
                user.setUserName(user.getFullName());

                userService.registerUser(user); // Gọi phương thức để lưu người dùng và vai trò

                return "redirect:/loginPage";
            }
    
        }
