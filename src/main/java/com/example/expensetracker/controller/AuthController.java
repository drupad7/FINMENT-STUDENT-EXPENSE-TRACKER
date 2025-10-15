package com.example.expensetracker.controller;

import com.example.expensetracker.dao.UserDAO;
import com.example.expensetracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // allow frontend access
public class AuthController {

    @Autowired
    private UserDAO userDAO;

    // ========================
    // REGISTER
    // ========================
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // Check if email already exists
        User existingUser = userDAO.findByEmail(user.getEmail());
        if (existingUser != null) {
            response.put("error", "Email already registered");
            return response;
        }

        // Save user
        userDAO.registerUser(user);
        response.put("message", "User registered successfully");
        response.put("redirectURL", "/login.html");
        return response;
    }

    // ========================
    // LOGIN
    // ========================
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();

        String email = credentials.get("email");
        String password = credentials.get("password");

        User user = userDAO.findByEmail(email);

        if (user == null) {
            response.put("error", "User not found");
            return response;
        }

        if (!user.getPassword().equals(password)) {
            response.put("error", "Invalid credentials");
            return response;
        }

        response.put("message", "Login successful");
        response.put("redirectURL", "/index.html"); // ✅ Go to main dashboard after login
        return response;
    }
}
