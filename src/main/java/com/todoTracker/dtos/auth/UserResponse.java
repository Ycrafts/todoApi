package com.todoTracker.dtos.auth; // Or com.todoTracker.dtos.user

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    // Add other relevant public fields here
}