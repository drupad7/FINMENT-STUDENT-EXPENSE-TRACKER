package com.example.expensetracker.controller;

import com.example.expensetracker.model.Expense;
import com.example.expensetracker.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping("/user/{email}")
    public List<Expense> getUserExpenses(@PathVariable String email) {
        return service.getExpenses(email);
    }

    @PostMapping("/add")
    public void addExpense(@RequestBody Expense expense) {
        service.addExpense(expense);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequest request) {
        service.transfer(request.getEmail(), request.getFromWallet(), request.getToWallet(), request.getAmount(), request.getDescription(), request.getDate());
    }

    // DTO for transfer request
    public static class TransferRequest {
        private String email;
        private String fromWallet;
        private String toWallet;
        private double amount;
        private String description;
        private String date;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFromWallet() { return fromWallet; }
        public void setFromWallet(String fromWallet) { this.fromWallet = fromWallet; }

        public String getToWallet() { return toWallet; }
        public void setToWallet(String toWallet) { this.toWallet = toWallet; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }
}
