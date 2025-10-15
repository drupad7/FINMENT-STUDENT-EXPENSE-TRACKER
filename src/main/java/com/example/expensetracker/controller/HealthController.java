package com.example.expensetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/test-db")
    public String testDbConnection() {
        try (Connection conn = dataSource.getConnection()) {
            return "✅ MySQL Connection Successful: " + conn.getMetaData().getURL();
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Database connection failed: " + e.getMessage();
        }
    }
}
