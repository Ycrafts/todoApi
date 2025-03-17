package com.todoTracker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todoTracker.models.TodoItem;
import com.todoTracker.models.TodoList;
import com.todoTracker.models.User;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem,Long> {
    List<TodoItem> findByCreatedBy(User user);
    List<TodoItem> findByTodoList(TodoList todoList);
}
