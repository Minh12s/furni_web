package com.example.furni.controllers.User;

import com.example.furni.entity.User;
import com.example.furni.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/MyOrder")
public class ChangePasswordController extends BaseController {

    @Autowired
    private UserService userService;

    @GetMapping("/ChangePassword")
    public String changePasswordForm(HttpSession session, Model model) {
        // Kiểm tra userId có tồn tại trong session hay không
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            session.setAttribute("errorMessage", "You must log in to change your password.");
            return "redirect:/login";
        }

        // Hiển thị thông báo thành công hoặc lỗi (nếu có)
        String successMessage = (String) session.getAttribute("successMessage");
        String errorMessage = (String) session.getAttribute("errorMessage");

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "MyOrder/ChangePassword";
    }


    @PostMapping("/ChangePassword")
    public String changePassword(HttpSession session, @RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("newPasswordConfirmation") String newPasswordConfirmation) {
        // Lấy userId từ session
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            session.setAttribute("errorMessage", "User not logged in.");
            return "redirect:/MyOrder/ChangePassword";
        }

        try {
            User user = userService.getUserById(userId);
            if (!userService.checkPassword(user, currentPassword)) {
                session.setAttribute("errorMessage", "Current password is incorrect.");
                return "redirect:/MyOrder/ChangePassword";
            }

            if (!newPassword.equals(newPasswordConfirmation)) {
                session.setAttribute("errorMessage", "New password and confirmation do not match.");
                return "redirect:/MyOrder/ChangePassword";
            }

            userService.changePassword(userId, newPassword);
            session.setAttribute("successMessage", "Password changed successfully!");
        } catch (RuntimeException e) {
            session.setAttribute("errorMessage", "Failed to change password: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/MyOrder/ChangePassword";
    }
}

