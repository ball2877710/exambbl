package com.bbl.exam.test.service;

import com.bbl.exam.test.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void getAllUsersSuccess() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    void getUserSuccessAndReturnUser() {
        Optional<User> user = userService.getUser(1L);
        assertTrue(user.isPresent());
        assertEquals("Leanne Graham", user.get().getName());
    }

    @Test
    void getUserFailAndReturnEmpty() {
        Optional<User> user = userService.getUser(99L);
        assertFalse(user.isPresent());
    }

    @Test
    void createUserWithNewId() {
        User newUser = new User();
        newUser.setName("Test User");
        newUser.setUsername("testuser");
        newUser.setEmail("test@user.com");

        User createdUser = userService.createUser(newUser);

        assertNotNull(createdUser);
        assertEquals(4L, createdUser.getId());
        assertEquals("Test User", createdUser.getName());

        Optional<User> retrievedUser = userService.getUser(4L);
        assertTrue(retrievedUser.isPresent());
        assertEquals("Test User", retrievedUser.get().getName());
    }

    @Test
    void updateUserSuccessAndReturnUser() {
        User updatedData = new User();
        updatedData.setName("Leanne Graham Updated");
        updatedData.setUsername("Bret");
        updatedData.setEmail("Sincere@april.biz");

        Optional<User> updatedUser = userService.updateUser(1L, updatedData);

        assertTrue(updatedUser.isPresent());
        assertEquals("Leanne Graham Updated", updatedUser.get().getName());
        assertEquals(1L, updatedUser.get().getId());

        Optional<User> retrievedUser = userService.getUser(1L);
        assertTrue(retrievedUser.isPresent());
        assertEquals("Leanne Graham Updated", retrievedUser.get().getName());
    }

    @Test
    void updateUserFailAndReturnEmpty() {
        User updatedData = new User();
        updatedData.setName("Non Existent");

        Optional<User> result = userService.updateUser(99L, updatedData);

        assertFalse(result.isPresent());
    }

    @Test
    void deleteUserSuccess() {
        boolean deleted = userService.deleteUser(1L);
        assertTrue(deleted);

        Optional<User> user = userService.getUser(1L);
        assertFalse(user.isPresent());
    }

    @Test
    void deleteUserFail() {
        boolean deleted = userService.deleteUser(99L);
        assertFalse(deleted);
    }
}
