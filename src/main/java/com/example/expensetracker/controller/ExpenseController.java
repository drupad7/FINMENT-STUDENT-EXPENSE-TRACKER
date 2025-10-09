package com.example.expensetracker.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final List<Map<String, Object>> transactions = new ArrayList<>();

    @GetMapping
    public List<Map<String, Object>> getAllExpenses() {
        return transactions;
    }

    @PostMapping
    public Map<String, Object> addExpense(@RequestBody Map<String, Object> expense) {
        expense.put("id", UUID.randomUUID().toString()); 
        transactions.add(expense);
        return expense;
    }

   
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

 
    @GetMapping("/month/{month}")
    public double getMonthlyTotal(@PathVariable String month) {
        double total = 0;
        for (Map<String, Object> exp : transactions) {
            String date = (String) exp.get("date"); 
            if (date != null && date.startsWith(month)) {
                Object amt = exp.get("amount");
                if (amt instanceof Number) {
                    total += ((Number) amt).doubleValue();
                }
            }
        }
        return total;
    }

   
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable String id) {
        boolean removed = transactions.removeIf(exp -> exp.get("id").equals(id));
        return removed ? "Deleted successfully!" : "Expense not found!";
    }
}

