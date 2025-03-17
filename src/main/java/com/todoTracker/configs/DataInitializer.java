package com.todoTracker.configs; // Or your preferred package

import com.todoTracker.models.TodoItem;
import com.todoTracker.models.TodoItem.TodoStatus;
import com.todoTracker.models.TodoList;
import com.todoTracker.models.User;
import com.todoTracker.repositories.TodoItemRepository;
import com.todoTracker.repositories.TodoListRepository;
import com.todoTracker.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@Profile("dev") // This will only run in the 'dev' profile
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TodoListRepository todoListRepository;
    private final TodoItemRepository todoItemRepository;
    private final PasswordEncoder passwordEncoder; // Inject your PasswordEncoder

    @Override
    public void run(String... args) throws Exception {
        // Create a test user
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword(passwordEncoder.encode("password123")); // Encode the password
        User savedTestUser = userRepository.save(testUser);

        // Create todo lists for the test user
        TodoList personalList = new TodoList();
        personalList.setName("Personal Errands");
        personalList.setDescription("Things I need to do for myself");
        personalList.setCreatedBy(savedTestUser);
        personalList.setCreatedAt(LocalDateTime.now());
        personalList.setUpdatedAt(LocalDateTime.now());
        TodoList savedPersonalList = todoListRepository.save(personalList);

        TodoList workList = new TodoList();
        workList.setName("Work Tasks");
        workList.setDescription("Tasks related to my job");
        workList.setCreatedBy(savedTestUser);
        workList.setCreatedAt(LocalDateTime.now());
        workList.setUpdatedAt(LocalDateTime.now());
        TodoList savedWorkList = todoListRepository.save(workList);

        // Create todo items for the personal list
        TodoItem buyMilk = new TodoItem();
        buyMilk.setName("Buy milk");
        buyMilk.setDescription("From the local supermarket");
        buyMilk.setTodoList(savedPersonalList);
        buyMilk.setDueDate(LocalDateTime.now().plusDays(5));
        buyMilk.setStatus(TodoStatus.PENDING);
        buyMilk.setCreatedBy(savedTestUser);
        buyMilk.setCreatedAt(LocalDateTime.now());
        buyMilk.setUpdatedAt(LocalDateTime.now());
        todoItemRepository.save(buyMilk);

        TodoItem payBills = new TodoItem();
        payBills.setName("Pay bills");
        payBills.setDescription("Electricity and internet");
        payBills.setTodoList(savedPersonalList);
        payBills.setDueDate(LocalDateTime.now().plusWeeks(1));
        payBills.setStatus(TodoStatus.PENDING);
        payBills.setCreatedBy(savedTestUser);
        payBills.setCreatedAt(LocalDateTime.now());
        payBills.setUpdatedAt(LocalDateTime.now());
        todoItemRepository.save(payBills);

        // Create todo items for the work list
        TodoItem preparePresentation = new TodoItem();
        preparePresentation.setName("Prepare presentation");
        preparePresentation.setDescription("For the Monday meeting");
        preparePresentation.setTodoList(savedWorkList);
        preparePresentation.setDueDate(LocalDateTime.now().plusDays(3));
        preparePresentation.setStatus(TodoStatus.PENDING);
        preparePresentation.setCreatedBy(savedTestUser);
        preparePresentation.setCreatedAt(LocalDateTime.now());
        preparePresentation.setUpdatedAt(LocalDateTime.now());
        todoItemRepository.save(preparePresentation);

        TodoItem sendReport = new TodoItem();
        sendReport.setName("Send weekly report");
        sendReport.setDescription("To the team");
        sendReport.setTodoList(savedWorkList);
        sendReport.setDueDate(LocalDateTime.now().plusDays(4));
        sendReport.setStatus(TodoStatus.PENDING);
        sendReport.setCreatedBy(savedTestUser);
        sendReport.setCreatedAt(LocalDateTime.now());
        sendReport.setUpdatedAt(LocalDateTime.now());
        todoItemRepository.save(sendReport);

        System.out.println("Sample data initialized...");
    }
}