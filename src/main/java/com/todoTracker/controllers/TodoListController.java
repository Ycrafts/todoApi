package com.todoTracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.todoTracker.dtos.TodoListDTO;
import com.todoTracker.services.TodoListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/todolists")
@PreAuthorize("isAuthenticated()") // Secure all endpoints in this controller
@RequiredArgsConstructor
public class TodoListController {

    private final TodoListService todoListService;

    @PostMapping
    public ResponseEntity<TodoListDTO> createTodoList(@RequestBody TodoListDTO todoListDTO) {
        TodoListDTO createdTodoList = todoListService.createTodoList(todoListDTO);
        return new ResponseEntity<>(createdTodoList, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TodoListDTO>> getAllTodoLists() {
        List<TodoListDTO> todoLists = todoListService.getAllTodoLists();
        return new ResponseEntity<>(todoLists, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoListDTO> getTodoListById(@PathVariable Long id) {
        TodoListDTO todoList = todoListService.getTodoListById(id);
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoListDTO> updateTodoList(@PathVariable Long id, @RequestBody TodoListDTO updatedTodoListDTO) {
        TodoListDTO updatedTodoList = todoListService.updateTodoList(id, updatedTodoListDTO);
        return new ResponseEntity<>(updatedTodoList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoList(@PathVariable Long id) {
        todoListService.deleteTodoList(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}