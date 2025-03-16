package com.todoTracker.dtos; // Or com.todoTracker.dtos.user

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    // Add other relevant public fields here
}