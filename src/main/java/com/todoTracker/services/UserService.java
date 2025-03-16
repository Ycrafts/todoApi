package com.todoTracker.services;

import com.todoTracker.dtos.auth.UpdateRequest;
import com.todoTracker.dtos.auth.UserResponse;
import com.todoTracker.models.User;
import com.todoTracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
    }

    public UserResponse getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = userOptional.get();
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }


    public User updateUser(Long id, UpdateRequest updateRequest) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User existingUser = existingUserOptional.get();

        if (updateRequest.getEmail() != null) {
            if (userRepository.findByEmail(updateRequest.getEmail()).isPresent() &&
                !existingUser.getEmail().equalsIgnoreCase(updateRequest.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
            }
            existingUser.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getUsername() != null) {
            if (userRepository.findByUsername(updateRequest.getUsername()).isPresent() &&
                !existingUser.getUsername().equalsIgnoreCase(updateRequest.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
            }
            existingUser.setUsername(updateRequest.getUsername());
        }

        return userRepository.save(existingUser);
    }
}