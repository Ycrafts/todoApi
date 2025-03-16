// package com.todo.todoapi.configs;


// import com.todo.todoapi.models.TodoList;
// import com.todo.todoapi.models.User;
// import com.todo.todoapi.repositories.TodoListRepository;
// import com.todo.todoapi.repositories.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import java.time.LocalDateTime;

// @Component
// public class TodoListInitializer implements CommandLineRunner {

//     @Autowired
//     private TodoListRepository todoListRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Override
//     public void run(String... args) throws Exception {
//         // Create test users (if not already present)
//         User user1 = userRepository.findByUsername("testuser1").orElse(null);
//         if (user1 == null) {
//             user1 = new User();
//             user1.setUsername("testuser1");
//             user1.setEmail("testuser1@example.com");
//             user1.setPassword("$2a$10$SOME_BCRYPT_HASH"); // Replace with a real BCrypt hash
//             userRepository.save(user1);
//         }

//         User user2 = userRepository.findByUsername("testuser2").orElse(null);
//         if (user2 == null) {
//             user2 = new User();
//             user2.setUsername("testuser2");
//             user2.setEmail("testuser2@example.com");
//             user2.setPassword("$2a$10$SOME_OTHER_BCRYPT_HASH"); // Replace with a real BCrypt hash
//             userRepository.save(user2);
//         }

//         // Create test TodoLists
//         if (todoListRepository.count() == 0) { // Check if TodoLists are already present
//             TodoList todo1 = new TodoList();
//             todo1.setName("Task 1");
//             todo1.setDescription("Description for Task 1");
//             todo1.setCreatedAt(LocalDateTime.now().minusDays(1));
//             todo1.setUpdatedAt(LocalDateTime.now());
//             todo1.setCreatedBy(user1);
//             todoListRepository.save(todo1);

//             TodoList todo2 = new TodoList();
//             todo2.setName("Task 2");
//             todo2.setDescription("Description for Task 2");
//             todo2.setCreatedAt(LocalDateTime.now().minusDays(2));
//             todo2.setUpdatedAt(LocalDateTime.now());
//             todo2.setCreatedBy(user1);
//             todoListRepository.save(todo2);

//             TodoList todo3 = new TodoList();
//             todo3.setName("Task 3");
//             todo3.setDescription("Description for Task 3");
//             todo3.setCreatedAt(LocalDateTime.now().minusDays(3));
//             todo3.setUpdatedAt(LocalDateTime.now());
//             todo3.setCreatedBy(user2);
//             todoListRepository.save(todo3);

//             TodoList todo4 = new TodoList();
//             todo4.setName("Future Task");
//             todo4.setDescription("Task that should be in the future");
//             todo4.setCreatedAt(LocalDateTime.now().plusDays(2));
//             todo4.setUpdatedAt(LocalDateTime.now().plusDays(3));
//             todo4.setCreatedBy(user1);
//             todoListRepository.save(todo4);
//         }
//     }
// }
