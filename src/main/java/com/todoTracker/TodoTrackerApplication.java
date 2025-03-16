package com.todoTracker;

import com.todoTracker.models.User;
import com.todoTracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RequiredArgsConstructor
public class TodoTrackerApplication {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(TodoTrackerApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeAdminUser() {
        return args -> {
            String adminUsername = "yonantan"; // Replace with your desired admin username
            String adminEmail = "yonatanassefa60@gmail.com"; // Replace with your desired admin email
            String adminPassword = "PassPass@123"; // Replace with a strong password

            Optional<User> adminUserOptional = userRepository.findByUsername(adminUsername);

            if (adminUserOptional.isEmpty()) {
                User adminUser = User.builder()
                        .username(adminUsername)
                        .email(adminEmail)
                        .password(passwordEncoder.encode(adminPassword))
                        .roles(List.of("ADMIN"))
                        .build();
                userRepository.save(adminUser);
                System.out.println("Admin user created successfully.");
            } else {
                System.out.println("Admin user already exists.");
            }
        };
    }
}