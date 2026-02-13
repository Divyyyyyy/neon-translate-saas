package com.example.smartexpense.service;

import com.example.smartexpense.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final Map<Long, User> usersById = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User addUser(String name) {
        Long id = idGenerator.getAndIncrement();
        User user = new User(id, name);
        usersById.put(id, user);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(usersById.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public void clearAll() {
        usersById.clear();
        idGenerator.set(1);
    }
}

