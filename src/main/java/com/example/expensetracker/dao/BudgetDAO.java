package com.example.expensetracker.dao;

import com.example.expensetracker.model.Budget;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class BudgetDAO {

    private final JdbcTemplate jdbcTemplate;

    public BudgetDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Budget> rowMapper = (ResultSet rs, int rowNum) -> {
        Budget b = new Budget();
        b.setId(rs.getInt("id"));
        b.setEmail(rs.getString("email"));
        b.setMonth(rs.getInt("month"));
        b.setYear(rs.getInt("year"));
        b.setLimitAmount(rs.getDouble("limit_amount"));
        return b;
    };

    // Fetch budget by email, month, year
    public Budget getBudget(String email, int month, int year) {
        String sql = "SELECT * FROM budgets WHERE email = ? AND month = ? AND year = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, email, month, year);
        } catch (Exception e) {
            return null; // no budget set
        }
    }

    // Add or update budget
    public void setBudget(Budget budget) {
        String sqlCheck = "SELECT COUNT(*) FROM budgets WHERE email = ? AND month = ? AND year = ?";
        Integer count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, budget.getEmail(), budget.getMonth(), budget.getYear());
        if (count != null && count > 0) {
            // update existing
            String sqlUpdate = "UPDATE budgets SET limit_amount = ? WHERE email = ? AND month = ? AND year = ?";
            jdbcTemplate.update(sqlUpdate, budget.getLimitAmount(), budget.getEmail(), budget.getMonth(), budget.getYear());
        } else {
            // insert new
            String sqlInsert = "INSERT INTO budgets (email, month, year, limit_amount) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sqlInsert, budget.getEmail(), budget.getMonth(), budget.getYear(), budget.getLimitAmount());
        }
    }
}
