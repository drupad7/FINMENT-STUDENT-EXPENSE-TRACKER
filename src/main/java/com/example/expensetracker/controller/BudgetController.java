package com.example.expensetracker.controller;

import com.example.expensetracker.model.Budget;
import com.example.expensetracker.service.BudgetService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // GET budget for specific month/year
    @GetMapping("/{email}/{month}")
    public Budget getBudget(@PathVariable String email, @PathVariable int month) {
        Budget b = budgetService.getMonthlyBudget(email, month, java.time.Year.now().getValue());
        if (b == null) return new Budget(email, month, java.time.Year.now().getValue(), 0.0);
        return b;
    }

    // POST to set or update budget
    @PostMapping("/set")
    public String setBudget(@RequestBody Budget budget) {
        budgetService.setMonthlyBudget(budget.getEmail(), budget.getLimitAmount(), budget.getMonth(), budget.getYear());
        return "Budget set successfully!";
    }
}
