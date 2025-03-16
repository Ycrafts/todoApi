// package com.todoTracker.configs;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import java.time.LocalDateTime;

// @Component
// public class TodoItemInitializer implements CommandLineRunner {

//     @Autowired
//     private TodoItemRepository todoItemRepository;

//     @Autowired
//     private TodoListRepository todoListRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Override
//     public void run(String... args) throws Exception {
//         // Fetch existing users
//         User user1 = userRepository.findByUsername("testuser1").orElse(null);
//         User user2 = userRepository.findByUsername("testuser2").orElse(null);

//         if (user1 == null || user2 == null) {
//             System.out.println("Users not found. Ensure TodoListInitializer runs first.");
//             return;
//         }

//         // Fetch an existing TodoList (or create a default one)
//         TodoList todoList = todoListRepository.findAll().stream().findFirst().orElse(null);
//         if (todoList == null) {
//             System.out.println("No TodoLists found. Ensure TodoListInitializer runs first.");
//             return;
//         }

//         // Create sample TodoItems only if they don't exist
//         if (todoItemRepository.count() == 0) {
//             TodoItem item1 = new TodoItem();
//             item1.setName("Buy groceries");
//             item1.setDescription("Milk, eggs, bread");
//             item1.setDueDate(LocalDateTime.now().plusDays(2));
//             item1.setTodoList(todoList);
//             item1.setCreatedBy(user1);
//             todoItemRepository.save(item1);

//             TodoItem item2 = new TodoItem();
//             item2.setName("Workout");
//             item2.setDescription("1-hour gym session");
//             item2.setDueDate(LocalDateTime.now().plusDays(1));
//             item2.setTodoList(todoList);
//             item2.setCreatedBy(user2);
//             todoItemRepository.save(item2);

//             TodoItem item3 = new TodoItem();
//             item3.setName("Study for exam");
//             item3.setDescription("Review algorithms and data structures");
//             item3.setDueDate(LocalDateTime.now().plusDays(5));
//             item3.setTodoList(todoList);
//             item3.setCreatedBy(user1);
//             todoItemRepository.save(item3);

//             System.out.println("Sample TodoItems added.");
//         }
//     }
// }
