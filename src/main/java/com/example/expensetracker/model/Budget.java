package com.example.expensetracker.model;

public class Budget {
    private int id;
    private String email;
    private int month;
    private int year;
    private double limitAmount;

    public Budget() {}

    public Budget(String email, int month, int year, double limitAmount) {
        this.email = email;
        this.month = month;
        this.year = year;
        this.limitAmount = limitAmount;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public double getLimitAmount() { return limitAmount; }
    public void setLimitAmount(double limitAmount) { this.limitAmount = limitAmount; }
}
