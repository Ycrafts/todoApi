package com.todoTracker.dtos.auth; // Or com.todoTracker.dtos.user

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
}