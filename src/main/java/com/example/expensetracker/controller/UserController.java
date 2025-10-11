package com.example.expensetracker.controller;

import com.example.expensetracker.dao.UserDAO;
import com.example.expensetracker.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private UserDAO UserDAO;

    public UserController(UserDAO UserDAO) {
        this.UserDAO = UserDAO;
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        User existingUser = UserDAO.findByEmail(user.getEmail());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return "Login successful!";
        } else {
            return "Invalid email or password!";
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        UserDAO.registerUser(user);
        return "User registered successfully!";
    }
}
