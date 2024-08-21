package com.example.furni.controllers.admin.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SercurityConfiguration {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        // Sử dụng email để lấy thông tin người dùng, sau đó kiểm tra bằng user_name
        userDetailsManager.setUsersByUsernameQuery("select user_name, password, is_active from user where email = ?");

        // Dùng user_name lấy từ email để tìm quyền trong bảng roles
        userDetailsManager.setAuthoritiesByUsernameQuery("select user_name, role from roles where user_name = ?");

        return userDetailsManager;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/loginAdmin").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/admin/loginAdmin")
                        .loginProcessingUrl("/authenticateAdmin")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureUrl("/admin/loginAdmin?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logoutAdmin")
                        .logoutSuccessUrl("/admin/loginAdmin")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

}
