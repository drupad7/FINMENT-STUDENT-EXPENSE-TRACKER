package com.example.expensetracker.controller;

import com.example.expensetracker.dao.UserDAO;
import com.example.expensetracker.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userDAO.registerUser(user);
        return "User registered successfully!";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        User existingUser = userDAO.findByEmail(user.getEmail());
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            return "success";
        } else {
            return "Invalid email or password!";
        }
    }
}
