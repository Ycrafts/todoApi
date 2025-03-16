package com.todoTracker.configs; // Keep the original package name if this is where you want it

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.todoTracker.models.User;
import com.todoTracker.repositories.UserRepository;

import java.util.List; // Import List instead of Set

@Component
public class UserInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setEmail("testuser@example.com");
            testUser.setPassword(passwordEncoder.encode("password123"));
            List<String> roles = List.of("USER"); 
            testUser.setRoles(roles);
            userRepository.save(testUser);
        }
    }
}