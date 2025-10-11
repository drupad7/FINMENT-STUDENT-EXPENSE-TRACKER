package com.example.expensetracker.controller;

import com.example.expensetracker.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/summary")
@CrossOrigin(origins = "*")
public class SummaryController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDAO userDAO;

    // ✅ API: Dashboard summary for given email
    @GetMapping
    public Map<String, Object> getDashboardSummary(@RequestParam String email) {

        Map<String, Object> response = new HashMap<>();

        // 1️⃣ Find userId from email
        Integer userId = userDAO.getUserIdByEmail(email);
        if (userId == null) {
            response.put("error", "User not found");
            return response;
        }

        // 2️⃣ Total income
        Double totalIncome = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount),0) FROM income WHERE user_id = ?",
                Double.class, userId);

        // 3️⃣ Total spent
        Double totalSpent = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount),0) FROM expenses WHERE user_id = ?",
                Double.class, userId);

        // 4️⃣ Wallet balances
        Double upiBalance = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount),0) FROM income WHERE user_id = ? AND wallet = 'UPI'",
                Double.class, userId);

        Double cashInHand = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount),0) FROM income WHERE user_id = ? AND wallet = 'Cash'",
                Double.class, userId);

        // 5️⃣ Net balance
        double netBalance = totalIncome - totalSpent;

        // 6️⃣ Monthly budget (optional)
        Double monthlyBudget = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(amount),0) FROM income WHERE user_id = ? AND MONTH(date) = MONTH(CURDATE())",
                Double.class, userId);

        // 7️⃣ Return all details
        response.put("userId", userId);
        response.put("totalIncome", totalIncome);
        response.put("totalSpent", totalSpent);
        response.put("upiBalance", upiBalance);
        response.put("cashInHand", cashInHand);
        response.put("netBalance", netBalance);
        response.put("monthlyBudget", monthlyBudget);

        return response;
    }
}
