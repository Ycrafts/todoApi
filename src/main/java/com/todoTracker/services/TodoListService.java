package com.todoTracker.services;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.todoTracker.models.TodoList;
import com.todoTracker.models.User;
import com.todoTracker.dtos.TodoListDTO;
import com.todoTracker.repositories.TodoListRepository;
import com.todoTracker.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoListService {

    private final TodoListRepository todoListRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof String) {
            String username = (String) authentication.getPrincipal();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        } else if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    public List<TodoListDTO> getAllTodoLists() {
        User currentUser = getCurrentUser();
        return todoListRepository.findByCreatedBy(currentUser).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TodoListDTO getTodoListById(Long id) {
        User currentUser = getCurrentUser();
        Optional<TodoList> todoListOptional = todoListRepository.findById(id);
    
        if (todoListOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo List not found");
        }
    
        TodoList todoList = todoListOptional.get();
    
        System.out.println("Retrieved TodoList createdBy ID: " + todoList.getCreatedBy().getId());
        System.out.println("Current User ID: " + currentUser.getId());
    
        if (!todoList.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this todo list");
        }
    
        return mapToDTO(todoList);
    }
    

    public TodoListDTO createTodoList(TodoListDTO todoListDTO) {
        User currentUser = getCurrentUser();
        TodoList todoList = new TodoList();
        todoList.setName(todoListDTO.getName());
        todoList.setDescription(todoListDTO.getDescription());
        todoList.setCreatedAt(LocalDateTime.now());
        todoList.setUpdatedAt(LocalDateTime.now());
        todoList.setCreatedBy(currentUser);
        TodoList savedTodoList = todoListRepository.save(todoList);
        return mapToDTO(savedTodoList);
    }

    public TodoListDTO updateTodoList(Long id, TodoListDTO updatedTodoListDTO) {
        User currentUser = getCurrentUser();
        return todoListRepository.findById(id)
                .map(existingTodoList -> {
                    if (!existingTodoList.getCreatedBy().getId().equals(currentUser.getId())) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this todo list");
                    }
                    existingTodoList.setName(updatedTodoListDTO.getName());
                    existingTodoList.setDescription(updatedTodoListDTO.getDescription());
                    existingTodoList.setUpdatedAt(LocalDateTime.now());
                    TodoList savedTodoList = todoListRepository.save(existingTodoList);
                    return mapToDTO(savedTodoList);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo List not found"));
    }

    public void deleteTodoList(Long id) {
        User currentUser = getCurrentUser();
        Optional<TodoList> todoListOptional = todoListRepository.findById(id);
        if (todoListOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo List not found");
        }
        TodoList todoList = todoListOptional.get();
        if (!todoList.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this todo list");
        }
        todoListRepository.deleteById(id);
    }

    private TodoListDTO mapToDTO(TodoList todoList) {
        TodoListDTO dto = new TodoListDTO();
        dto.setId(todoList.getId());
        dto.setName(todoList.getName());
        dto.setDescription(todoList.getDescription());
        dto.setCreatedAt(todoList.getCreatedAt());
        dto.setUpdatedAt(todoList.getUpdatedAt());
        if (todoList.getCreatedBy() != null) {
            dto.setCreatedBy(mapUserToUserResponseDTO(todoList.getCreatedBy()));
        }
        return dto;
    }

    private com.todoTracker.dtos.auth.UserResponse mapUserToUserResponseDTO(User user) {
        com.todoTracker.dtos.auth.UserResponse userResponse = new com.todoTracker.dtos.auth.UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}