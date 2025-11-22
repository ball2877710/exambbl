package com.bbl.exam.test.controller;

import com.bbl.exam.test.model.User;
import com.bbl.exam.test.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUser(id);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {

        if (isInvalid(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "name, username, and email are required"));
        }

        User created = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {

        if (isInvalid(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "name, username, and email are required"));
        }

        return userService.updateUser(id, user)
                .<ResponseEntity<Object>>map(ResponseEntity::ok) //if Optional<User> have value convert to 200 OK
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
        return ResponseEntity.noContent().build();
    }

    private boolean isInvalid(User u) {
        return u.getName() == null || u.getName().isBlank()
                || u.getUsername() == null || u.getUsername().isBlank()
                || u.getEmail() == null || u.getEmail().isBlank();
    }
}