package com.example.expensetracker.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    // In-memory list (no database for now)
    private final List<Map<String, Object>> transactions = new ArrayList<>();

    // ✅ Get all expenses
    @GetMapping
    public List<Map<String, Object>> getAllExpenses() {
        return transactions;
    }

    // ✅ Add a new expense
    @PostMapping
    public Map<String, Object> addExpense(@RequestBody Map<String, Object> expense) {
        expense.put("id", UUID.randomUUID().toString()); // generate unique ID
        transactions.add(expense);
        return expense;
    }

    // ✅ Get expenses for a specific date (yyyy-MM-dd)
    @GetMapping("/date/{date}")
    public List<Map<String, Object>> getExpensesByDate(@PathVariable String date) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> exp : transactions) {
            if (date.equals(exp.get("date"))) {
                result.add(exp);
            }
        }
        return result;
    }

    // ✅ Get total for a month (yyyy-MM)
    @GetMapping("/month/{month}")
    public double getMonthlyTotal(@PathVariable String month) {
        double total = 0;
        for (Map<String, Object> exp : transactions) {
            String date = (String) exp.get("date"); // format: yyyy-MM-dd
            if (date != null && date.startsWith(month)) {
                Object amt = exp.get("amount");
                if (amt instanceof Number) {
                    total += ((Number) amt).doubleValue();
                }
            }
        }
        return total;
    }

    // ✅ Delete an expense by ID
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable String id) {
        boolean removed = transactions.removeIf(exp -> exp.get("id").equals(id));
        return removed ? "Deleted successfully!" : "Expense not found!";
    }
}
