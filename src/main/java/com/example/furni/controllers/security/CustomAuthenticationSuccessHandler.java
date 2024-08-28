package com.example.furni.controllers.security;

import com.example.furni.entity.User;
import com.example.furni.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Lấy username từ userDetails và tìm User theo username
        String username = userDetails.getUsername();
        User user = userRepository.findByUserName(username); // Giả sử bạn có phương thức findByUserName

        if (user != null) {
            // Thiết lập thông tin hiển thị fullName
            request.getSession().setAttribute("fullName", user.getFullName());
            // Thiết lập thông tin id vào session
            request.getSession().setAttribute("userId", user.getId());
        }

        // Redirect tới trang chủ hoặc trang nào khác sau khi login thành công
        response.sendRedirect( "/");
    }
}
