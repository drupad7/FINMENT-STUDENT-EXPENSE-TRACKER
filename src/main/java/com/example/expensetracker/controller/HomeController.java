package com.example.expensetracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // loads templates/index.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // loads templates/login.html
    }
}