package com.example.expensetracker.controller;

import com.example.expensetracker.dao.IncomeDAO;
import com.example.expensetracker.model.Income;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/income")
@CrossOrigin(origins = "http://localhost:5500") // Adjust if frontend runs on a different port
public class IncomeController {

    @Autowired
    private IncomeDAO incomeDAO;

    @PostMapping("/add")
    public ResponseEntity<String> addIncome(@RequestParam int userId,
                                            @RequestParam double amount,
                                            @RequestParam String source,
                                            @RequestParam String date,
                                            @RequestParam String paymentMethod) {
        incomeDAO.addIncome(userId, amount, source, date, paymentMethod);
        return new ResponseEntity<>("Income added successfully", HttpStatus.OK);
    }

    @GetMapping("/total")
    public double getTotalIncome(@RequestParam int userId) {
        return incomeDAO.getTotalIncome(userId);
    }

    @GetMapping("/all")
    public List<Income> getAllIncomes(@RequestParam int userId) {
        return incomeDAO.getAllIncomes(userId);
    }
}
