package com.example.furni.controllers.configuration;

import com.example.furni.controllers.security.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class UserSecurityConfiguration {
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public UserSecurityConfiguration(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery("select user_name, password, is_active from user where email = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery("select user_name, role from roles where user_name = ?");

        return userDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/fonts/**", "/scss/**", "/js/**").permitAll()
                        .requestMatchers("/loginPage", "/registers").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/authenticateUser")
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/loginPage?error=true")
                        .permitAll()
                )
                .logout(config -> config
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/loginPage")
                        .invalidateHttpSession(true) // Xóa session
                        .deleteCookies("JSESSIONID") // Xóa cookie session
                        .permitAll()
//                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .accessDeniedPage("/error/404")
                );
        return http.build();
    }


}