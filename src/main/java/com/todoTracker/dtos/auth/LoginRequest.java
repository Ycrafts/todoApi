package com.todoTracker.dtos.auth;


import lombok.Data;

@Data
public class LoginRequest {
    private String usernameOrEmail;
    private String password;
}