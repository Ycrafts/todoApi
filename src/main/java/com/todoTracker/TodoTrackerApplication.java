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

    public static void main(String[] args) {
        SpringApplication.run(TodoTrackerApplication.class, args);
    }
}