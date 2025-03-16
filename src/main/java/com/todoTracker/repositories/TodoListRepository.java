package com.todoTracker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todoTracker.models.TodoList;
import com.todoTracker.models.User;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long>{
    List<TodoList> findByCreatedBy(User createdBy);
}