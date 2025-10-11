package com.example.expensetracker.controller;

import com.example.expensetracker.dao.BudgetDAO;
import com.example.expensetracker.dao.UserDAO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/budget")
@CrossOrigin(origins = "*")
public class BudgetController {

    private final BudgetDAO budgetDao;
    private final UserDAO userDao;

    public BudgetController(BudgetDAO budgetDao, UserDAO userDao) {
        this.budgetDao = budgetDao;  // ✅ Corrected assignment
        this.userDao = userDao;      // ✅ Corrected assignment
    }

    // ✅ Fetch monthly summary
    @GetMapping("/{email}/{month}")
    public Map<String, Object> getMonthlyBudget(@PathVariable String email, @PathVariable String month) {
        Integer userId = userDao.getUserIdByEmail(email);
        if (userId == null) {
            return Map.of("error", "User not found for email: " + email);
        }
        return budgetDao.getMonthlySummary(userId, month);
    }

    // ✅ Set or update monthly budget
    @PostMapping("/set")
    public Map<String, Object> setBudget(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        String month = (String) payload.get("month");
        double limit = Double.parseDouble(payload.get("limit").toString());

        Integer userId = userDao.getUserIdByEmail(email);
        if (userId == null) {
            return Map.of("error", "User not found for email: " + email);
        }

        budgetDao.setOrUpdateBudget(userId, month, limit);
        return Map.of("message", "Budget set successfully", "limit", limit);
    }
}
