package com.example.expensetracker.controller;

import com.example.expensetracker.dao.ExpenseDAO;
import com.example.expensetracker.dao.UserDAO;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5500") // allow frontend at port 5500
public class ExpenseController {

    @Autowired
    private ExpenseDAO expenseDAO;

    @Autowired
    private UserDAO userDAO;

    // ➕ Add Expense
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> addExpenseByEmail(@RequestBody ExpenseRequest req) {
        Map<String, String> response = new HashMap<>();

        try {
            if (req.getEmail() == null || req.getEmail().isEmpty()) {
                response.put("message", "Email is required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            User user = userDAO.findByEmail(req.getEmail());
            if (user == null) {
                response.put("message", "User not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Expense expense = new Expense();
            expense.setUserId(user.getId());
            expense.setCategory(req.getCategory() == null ? "Other" : req.getCategory());
            expense.setAmount(req.getAmount());
            expense.setDescription(req.getNote() == null ? "" : req.getNote());

            if (req.getDate() != null && !req.getDate().isEmpty()) {
                expense.setDate(LocalDate.parse(req.getDate()));
            } else {
                expense.setDate(LocalDate.now());
            }

            int result = expenseDAO.save(expense);
            response.put("message", (result > 0) ? "Expense saved successfully" : "Error saving expense");
            return (result > 0)
                    ? ResponseEntity.ok(response)
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Server error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 📜 Get all expenses by user email
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Expense>> getExpensesByEmail(@PathVariable String email) {
        List<Expense> expenses = expenseDAO.findByUserEmail(email);
        return ResponseEntity.ok(expenses);
    }

    // ❌ Delete expense by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteExpense(@PathVariable int id) {
        int result = expenseDAO.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", (result > 0) ? "Expense deleted successfully" : "Error deleting expense");
        return (result > 0)
                ? ResponseEntity.ok(response)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // DTO for frontend
    public static class ExpenseRequest {
        private String email;
        private double amount;
        private String category;
        private String wallet;
        private String note;
        private String date;

        // Getters & Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public String getWallet() { return wallet; }
        public void setWallet(String wallet) { this.wallet = wallet; }

        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }

        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }
}
