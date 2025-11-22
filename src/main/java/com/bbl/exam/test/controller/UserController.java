package com.bbl.exam.test.controller;

import com.bbl.exam.test.model.User;
import com.bbl.exam.test.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userService.getAllUsers();
        log.info("Found {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        log.info("Fetching user with id {}", id);
        Optional<User> userOptional = userService.getUser(id);
        if (userOptional.isPresent()) {
            log.info("User found: {}", userOptional.get());
            return ResponseEntity.ok(userOptional.get());
        } else {
            log.warn("User not found with id {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        log.info("Creating user: {}", user);

        if (isInvalid(user)) {
            log.warn("Invalid user data: {}", user);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "name, username, and email are required"));
        }

        User created = userService.createUser(user);
        log.info("User created successfully: {}", created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        log.info("Updating user with id {}: {}", id, user);

        if (isInvalid(user)) {
            log.warn("Invalid user data for update: {}", user);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "name, username, and email are required"));
        }

        return userService.updateUser(id, user)
                .<ResponseEntity<Object>>map(updated -> {
                    log.info("User updated successfully: {}", updated);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    log.warn("User not found for update with id {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "User not found"));
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id {}", id);
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            log.warn("User not found for delete with id {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
        log.info("User deleted successfully with id {}", id);
        return ResponseEntity.noContent().build();
    }

    private boolean isInvalid(User u) {
        return u.getName() == null || u.getName().isBlank()
                || u.getUsername() == null || u.getUsername().isBlank()
                || u.getEmail() == null || u.getEmail().isBlank();
    }
}