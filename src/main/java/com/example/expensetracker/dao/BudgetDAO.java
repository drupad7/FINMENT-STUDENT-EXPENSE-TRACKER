package com.example.expensetracker.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class BudgetDAO {
    private final JdbcTemplate jdbcTemplate;

    public BudgetDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Double getBudgetLimit(int userId, String month) {
        String sql = "SELECT limit_amount FROM budgets WHERE user_id = ? AND month = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Double.class, userId, month);
        } catch (Exception e) {
            return null;
        }
    }

    public void setOrUpdateBudget(int userId, String month, double limitAmount) {
        String checkSql = "SELECT COUNT(*) FROM budgets WHERE user_id = ? AND month = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, userId, month);

        if (count > 0) {
            String updateSql = "UPDATE budgets SET limit_amount = ? WHERE user_id = ? AND month = ?";
            jdbcTemplate.update(updateSql, limitAmount, userId, month);
        } else {
            String insertSql = "INSERT INTO budgets (user_id, month, limit_amount) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSql, userId, month, limitAmount);
        }
    }

    public double getTotalSpent(int userId, String month) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE user_id = ? AND MONTH(date) = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, userId, month);
    }

    public Map<String, Object> getMonthlySummary(int userId, String month) {
        Double limit = getBudgetLimit(userId, month);
        double spent = getTotalSpent(userId, month);
        double remaining = (limit == null) ? 0 : (limit - spent);
        return Map.of("limit", limit != null ? limit : 0, "spent", spent, "remaining", remaining);
    }
}
