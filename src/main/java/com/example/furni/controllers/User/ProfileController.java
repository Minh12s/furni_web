package com.example.furni.controllers.User;

import com.example.furni.entity.User;
import com.example.furni.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/MyOrder")
public class ProfileController extends BaseController {
    @Autowired
    private UserService userService;

    @GetMapping("/Profile")
    public String profile(Model model, HttpSession session) {
        // Lấy userId từ session
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
        }

        return "MyOrder/Profile";
    }
    @GetMapping("/EditProfile")
    public String showEditProfile(Model model, HttpSession session) {
        // Lấy userId từ session
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId != null) {
            // Tìm người dùng theo userId
            User user = userService.findById(userId);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login"; // Hoặc trả về trang thông báo lỗi
        }

        return "MyOrder/EditProfile"; // Trả về trang chỉnh sửa hồ sơ
    }

    @PostMapping("/UpdateProfile")
    public String editProfile(
            @RequestParam("fullname") String fullName,
            @RequestParam("email") String email,
            @RequestParam("tel") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("thumbnail") MultipartFile thumbnailFile,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Lấy userId từ session
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Tìm người dùng theo userId
        User user = userService.findById(userId);
        if (user == null) {
            return "redirect:/login";
        }

        // Cập nhật thông tin người dùng
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);

        // Xử lý ảnh đại diện
        if (!thumbnailFile.isEmpty()) {
            try {
                // Đọc nội dung ảnh và chuyển sang base64
                byte[] imageBytes = thumbnailFile.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                user.setThumbnail(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Error uploading image.");
                return "redirect:/MyOrder/EditProfile";
            }
        }

        // Lưu thay đổi người dùng
        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/MyOrder/Profile";
    }

}
