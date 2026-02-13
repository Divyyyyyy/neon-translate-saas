package com.example.smartexpense.controller;

import com.example.smartexpense.dto.AddUserRequest;
import com.example.smartexpense.model.User;
import com.example.smartexpense.service.DebtService;
import com.example.smartexpense.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin // allow frontend from different origin during dev
public class UserController {

    private final UserService userService;
    private final DebtService debtService;

    public UserController(UserService userService, DebtService debtService) {
        this.userService = userService;
        this.debtService = debtService;
    }

    @PostMapping
    public User addUser(@RequestBody AddUserRequest request) {
        return userService.addUser(request.getName());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/reset")
    public void resetAll() {
        userService.clearAll();
        debtService.clearAll();
    }
}

