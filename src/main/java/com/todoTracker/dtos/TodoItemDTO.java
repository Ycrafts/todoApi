package com.todoTracker.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.todoTracker.models.TodoItem.TodoStatus;
import com.todoTracker.dtos.auth.UserResponse; // Assuming you have this DTO

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TodoItemDTO {

    private Long id;
    private String name;
    private String description;
    private TodoStatus status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long todoListId; // To associate with a TodoList
    private UserResponse createdBy; // To show who created it
}