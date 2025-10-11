package com.example.expensetracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SummaryDAO {
    private final JdbcTemplate jdbcTemplate;

    public SummaryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Total Income
    public Double getTotalIncome(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM income WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }

    // Total Expense
    public Double getTotalExpense(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }

    // Cash in Hand
    public Double getCashInHand(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM wallet WHERE user_id = ? AND type='cash'";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }

    // UPI Balance
    public Double getUpiBalance(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM wallet WHERE user_id = ? AND type='upi'";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }

    // Net Balance (income - expenses)
    public Double getNetBalance(int userId) {
        return getTotalIncome(userId) - getTotalExpense(userId);
    }
}
