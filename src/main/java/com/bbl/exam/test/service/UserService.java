package com.bbl.exam.test.service;

import com.bbl.exam.test.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public UserService() {
        users.put(1L, new User(1L, "Leanne Graham", "Bret",
                "Sincere@april.biz", "1-770-736-8031 x56442", "hildegard.org"));

        users.put(2L, new User(2L, "Ervin Howell", "Antonette",
                "Shanna@melissa.tv", "010-692-6593 x09125", "anastasia.net"));

        users.put(3L, new User(3L, "Clementine Bauch", "Samantha",
                "Nathan@yesenia.net", "1-463-123-4447", "ramiro.info"));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public User createUser(User user) {
        long newId = users.keySet().stream().max(Long::compare).orElse(0L) + 1;
        user.setId(newId);
        users.put(newId, user);
        return user;
    }

    public Optional<User> updateUser(Long id, User newUserData) {
        if (!users.containsKey(id)) return Optional.empty();
        newUserData.setId(id);
        users.put(id, newUserData);
        return Optional.of(newUserData);
    }

    public boolean deleteUser(Long id) {
        return users.remove(id) != null;
    }
}

