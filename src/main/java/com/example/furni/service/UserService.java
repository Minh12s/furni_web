package com.example.furni.service;

import com.example.furni.entity.Role;
import com.example.furni.entity.User;
import com.example.furni.repository.RoleRepository;
import com.example.furni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        user.setActive(true); // Hoặc user.setActive(1);

        // Lưu người dùng vào database
        userRepository.save(user);

        // Tạo và lưu vai trò USER
        Role userRole = new Role();
        userRole.setUserName(user); // Thiết lập đối tượng User cho vai trò
        userRole.setRole("ROLE_USER");

        roleRepository.save(userRole); // Lưu vai trò vào database
    }


}
