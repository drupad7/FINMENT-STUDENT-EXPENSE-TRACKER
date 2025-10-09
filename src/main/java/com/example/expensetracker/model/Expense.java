package com.example.expensetracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class Expense {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private double amount;
    private String category;
    private String description;

    public Expense() {}

    public Expense(LocalDate date, double amount, String category, String description) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
