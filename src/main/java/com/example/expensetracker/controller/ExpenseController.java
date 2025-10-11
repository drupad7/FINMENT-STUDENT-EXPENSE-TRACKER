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
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseDAO expenseDAO;

    @Autowired
    private UserDAO userDAO;

    // ➕ Add new expense (frontend posts to /api/expenses/add with { email, amount, category, wallet, note, date })
    @PostMapping("/add")
    public ResponseEntity<String> addExpenseByEmail(@RequestBody ExpenseRequest req) {
        try {
            if (req.getEmail() == null || req.getEmail().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email required");
            }

            User user = userDAO.findByEmail(req.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }

            Expense expense = new Expense();
            expense.setUserId(user.getId());
            expense.setCategory(req.getCategory() == null ? "Other" : req.getCategory());
            expense.setAmount(req.getAmount());
            expense.setDescription(req.getNote() == null ? "" : req.getNote());

            // parse date if provided (frontend date format is yyyy-MM-dd)
            if (req.getDate() != null && !req.getDate().isEmpty()) {
                expense.setDate(LocalDate.parse(req.getDate()));
            } else {
                expense.setDate(LocalDate.now());
            }

            int result = expenseDAO.save(expense);
            return (result > 0) ? ResponseEntity.ok("Expense saved successfully") :
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving expense");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    // Existing API: get by numeric user id (keeps backward compatibility)
    @GetMapping("/{userId}")
    public List<Expense> getExpensesById(@PathVariable int userId) {
        return expenseDAO.findByUserId(userId);
    }

    // NEW: Get all expenses for a user by email
    @GetMapping("/user/{email}")
    public List<Expense> getExpensesByEmail(@PathVariable String email) {
        return expenseDAO.findByUserEmail(email);
    }

    // ❌ Delete expense by ID
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable int id) {
        int result = expenseDAO.deleteById(id);
        return (result > 0) ? "Expense deleted" : "Error deleting expense";
    }

    // Inner static DTO class to receive frontend POST body
    public static class ExpenseRequest {
        private String email;
        private double amount;
        private String category;
        private String wallet; // optional, saved client-side logic only
        private String note;
        private String date;

        // getters and setters
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
