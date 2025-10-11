package com.example.expensetracker.model;

public class Income {
    private int id;
    private int userId; // ✅ add this
    private double amount;
    private String wallet;
    private String source;
    private String date;

    // ✅ Add a constructor (optional)
    public Income() {}

    public Income(int id, double amount, String wallet, String source, String date) {
        this.id = id;
        this.amount = amount;
        this.wallet = wallet;
        this.source = source;
        this.date = date;
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getWallet() { return wallet; }
    public void setWallet(String wallet) { this.wallet = wallet; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
