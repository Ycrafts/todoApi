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

import com.todoTracker.dtos.TodoItemDTO;
import com.todoTracker.dtos.TodoListDTO;
import com.todoTracker.dtos.auth.UserResponse;
import com.todoTracker.models.TodoItem;
import com.todoTracker.models.TodoItem.TodoStatus;
import com.todoTracker.models.TodoList;
import com.todoTracker.models.User;
import com.todoTracker.repositories.TodoItemRepository;
import com.todoTracker.repositories.TodoListRepository;
import com.todoTracker.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final UserRepository userRepository;
    private final TodoListRepository todoListRepository;

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

    public TodoItemDTO createTodoItem(TodoItemDTO todoItemDTO) {
        User currentUser = getCurrentUser();

        Optional<TodoList> todoListOptional = todoListRepository.findById(todoItemDTO.getTodoListId());
        if (todoListOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo List not found");
        }
        TodoList todoList = todoListOptional.get();

        if (!todoList.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to add items to this todo list");
        }

        TodoItem todoItem = new TodoItem();
        todoItem.setName(todoItemDTO.getName());
        todoItem.setDescription(todoItemDTO.getDescription());
        todoItem.setDueDate(todoItemDTO.getDueDate());
        todoItem.setCreatedAt(LocalDateTime.now());
        todoItem.setUpdatedAt(LocalDateTime.now());
        todoItem.setTodoList(todoList);
        todoItem.setCreatedBy(currentUser);

        if (todoItemDTO.getStatus() == null) {
            todoItem.setStatus(TodoStatus.PENDING);
        } else {
            todoItem.setStatus(todoItemDTO.getStatus());
        }

        TodoItem savedTodoItem = todoItemRepository.save(todoItem);
        return mapToDTO(savedTodoItem);
    }

    public List<TodoItemDTO> getAllTodoItems() {
        User currentUser = getCurrentUser();
        return todoItemRepository.findByCreatedBy(currentUser).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TodoItemDTO getTodoItemById(Long id) {
        User currentUser = getCurrentUser();
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(id);

        if (todoItemOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo Item not found");
        }

        TodoItem todoItem = todoItemOptional.get();

       //the current user owns the TodoList of this TodoItem
        if (!todoItem.getTodoList().getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access this todo item");
        }

        return mapToDTO(todoItem);
    }

    public List<TodoItemDTO> getAllTodoItemsForTodoList(Long todoListId) {
        User currentUser = getCurrentUser();
        Optional<TodoList> todoListOptional = todoListRepository.findById(todoListId);

        if (todoListOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo List not found");
        }
        TodoList todoList = todoListOptional.get();

        // the current user owns this TodoList
        if (!todoList.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to access items in this todo list");
        }

        List<TodoItem> todoItems = todoItemRepository.findByTodoList(todoList);
        return todoItems.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TodoItemDTO updateTodoItem(Long id, TodoItemDTO updatedTodoItemDTO) {
        User currentUser = getCurrentUser();
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(id);

        if (todoItemOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo Item not found");
        }

        TodoItem existingTodoItem = todoItemOptional.get();

        // the current user owns the TodoList of this TodoItem
        if (!existingTodoItem.getTodoList().getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to update this todo item");
        }

        existingTodoItem.setName(updatedTodoItemDTO.getName());
        existingTodoItem.setDescription(updatedTodoItemDTO.getDescription());
        existingTodoItem.setStatus(updatedTodoItemDTO.getStatus());
        existingTodoItem.setDueDate(updatedTodoItemDTO.getDueDate());
        existingTodoItem.setUpdatedAt(LocalDateTime.now());

        TodoItem updatedTodoItem = todoItemRepository.save(existingTodoItem);
        return mapToDTO(updatedTodoItem);
    }

    public void deleteTodoItem(Long id) {
        User currentUser = getCurrentUser();
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(id);

        if (todoItemOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo Item not found");
        }

        TodoItem todoItem = todoItemOptional.get();

       // the current user owns the TodoList of this TodoItem
        if (!todoItem.getTodoList().getCreatedBy().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this todo item");
        }

        todoItemRepository.deleteById(id);
    }

    private TodoItemDTO mapToDTO(TodoItem todoItem) {
        UserResponse creator = new UserResponse(
                todoItem.getCreatedBy().getId(),
                todoItem.getCreatedBy().getUsername(),
                todoItem.getCreatedBy().getEmail()
        );
        return new TodoItemDTO(
                todoItem.getId(),
                todoItem.getName(),
                todoItem.getDescription(),
                todoItem.getStatus(),
                todoItem.getDueDate(),
                todoItem.getCreatedAt(),
                todoItem.getUpdatedAt(),
                todoItem.getTodoList().getId(),
                creator
        );
    }
}