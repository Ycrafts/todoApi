package com.todoTracker.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoTracker.dtos.auth.RegisterRequest;
import com.todoTracker.dtos.auth.RegisterResponse;
import com.todoTracker.dtos.auth.UpdateRequest;
import com.todoTracker.dtos.auth.UserResponse;
import com.todoTracker.models.User;
import com.todoTracker.repositories.UserRepository;
import com.todoTracker.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Operations related to user management")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService; // Inject UserService

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return new ResponseEntity<>(new RegisterResponse("Username already exists"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ResponseEntity<>(new RegisterResponse("Email already exists"), HttpStatus.BAD_REQUEST);
        }

        User user = User.builder() // basically a constructor
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        return new ResponseEntity<>(new RegisterResponse("User registered successfully"), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user by ID (Admin only)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() and (#id == authentication.principal.id or hasRole('ADMIN'))") // current user's id or admin
    @Operation(summary = "Get a user by ID (Self or Admin)")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/")
    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        List<UserResponse> userResponse = userService.getAllUsers();
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get the currently logged-in user")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();
        Long userId = null;

        if (principal instanceof User) {
            userId = ((User) principal).getId();
        } else {
            String username = principal.toString();
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            userId = userOptional.get().getId();
        }

        if (userId != null) {
            return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated() and (#id == authentication.principal.id or hasRole('ADMIN'))")
    @Operation(summary = "Update a user by ID (Self or Admin)")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(id, updateRequest);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(updatedUser.getId());
        userResponse.setUsername(updatedUser.getUsername());
        userResponse.setEmail(updatedUser.getEmail());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/test")
    @Operation(summary = "Test endpoint (No authentication required)")
    public String test() {
        return new String("success");
    }
}