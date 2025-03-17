package com.todoTracker.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoTracker.dtos.auth.AuthResponse;
import com.todoTracker.dtos.auth.LoginRequest;
import com.todoTracker.services.UserService;
import com.todoTracker.util.JwtUtil;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsernameOrEmail(),
                        request.getPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userService.loadUserByUsername(request.getUsernameOrEmail());
            String jwtToken = jwtUtil.generateToken(userDetails);
            AuthResponse response = new AuthResponse();
            response.setToken(jwtToken);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).build(); 
        }
    }
}