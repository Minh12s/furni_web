package com.example.furni.service;

import com.example.furni.entity.Role;
import com.example.furni.entity.User;
import com.example.furni.repository.RoleRepository;
import com.example.furni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Đặt giá trị mặc định cho is_active
        user.setActive(true);

        // Lưu người dùng vào database
        userRepository.save(user);

        // Tạo và lưu vai trò USER
        Role userRole = new Role();
        userRole.setUserName(user); // Gán đối tượng User vào thuộc tính userName
        userRole.setRole("ROLE_USER"); // Đặt vai trò là USER

        roleRepository.save(userRole);
    }
    public User getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }


    public void changePassword(int userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}

