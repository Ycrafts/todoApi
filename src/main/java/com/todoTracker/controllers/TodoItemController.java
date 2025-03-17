package com.todoTracker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.todoTracker.dtos.TodoItemDTO;
import com.todoTracker.services.TodoItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/todo-items")
@RequiredArgsConstructor
public class TodoItemController {

    private final TodoItemService todoItemService;

    @PostMapping
    public ResponseEntity<TodoItemDTO> createTodoItem(@RequestBody TodoItemDTO todoItemDTO) {
        TodoItemDTO createdTodoItem = todoItemService.createTodoItem(todoItemDTO);
        return new ResponseEntity<>(createdTodoItem, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TodoItemDTO>> getAllTodoItems(){
        List<TodoItemDTO> todoItems = todoItemService.getAllTodoItems();
        return new ResponseEntity<>(todoItems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoItemDTO> getTodoItemById(@PathVariable Long id) {
        TodoItemDTO todoItem = todoItemService.getTodoItemById(id);
        return new ResponseEntity<>(todoItem, HttpStatus.OK);
    }

    @GetMapping("/lists/{todoListId}")
    public ResponseEntity<List<TodoItemDTO>> getAllTodoItemsForTodoList(@PathVariable Long todoListId) {
        List<TodoItemDTO> todoItems = todoItemService.getAllTodoItemsForTodoList(todoListId);
        return new ResponseEntity<>(todoItems, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoItemDTO> updateTodoItem(@PathVariable Long id, @RequestBody TodoItemDTO updatedTodoItemDTO) {
        TodoItemDTO updatedTodoItem = todoItemService.updateTodoItem(id, updatedTodoItemDTO);
        return new ResponseEntity<>(updatedTodoItem, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodoItem(@PathVariable Long id) {
        todoItemService.deleteTodoItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}