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

        import java.util.ArrayList;
        import java.util.List;


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
            public String register(@ModelAttribute("user") User user, Model model) {
                List<String> errorRegister = new ArrayList<>();

                // Kiểm tra Full Name
                if (user.getFullName().trim().isEmpty()) {
                    errorRegister.add("Full name cannot be  blank.");
                } else if (userService.isFullNameExists(user.getFullName())) {
                    errorRegister.add("This Full Name already exists.");
                }

                // Kiểm tra Email
                if (user.getEmail().trim().isEmpty()) {
                    errorRegister.add("Email cannot be blank.");
                } else if (!userService.isValidEmail(user.getEmail())) {
                    errorRegister.add("Please enter correct email format.");
                } else if (userService.isEmailExists(user.getEmail())) {
                    errorRegister.add("This email already exists.");
                }

                // Kiểm tra Password
                if (user.getPassword().trim().isEmpty()) {
                    errorRegister.add("Password cannot be blank.");
                }

                // Nếu có lỗi, trả về trang đăng ký và hiển thị lỗi
                if (!errorRegister.isEmpty()) {
                    model.addAttribute("errorRegister", errorRegister);
                    return "User/register";
                }

                // Nếu không có lỗi, lưu user và điều hướng tới trang đăng nhập
                userService.saveUser(user);
                return "redirect:/Page/Login";
            }

        }
