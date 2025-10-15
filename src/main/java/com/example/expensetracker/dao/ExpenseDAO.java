package com.example.expensetracker.dao;

import com.example.expensetracker.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExpenseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Add expense/income
    public void addExpense(Expense e) {
        String sql = "INSERT INTO expenses (email, amount, category, wallet, description, date) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, e.getEmail(), e.getAmount(), e.getCategory(), e.getWallet(), e.getDescription(), e.getDate());
    }

    // Get all expenses for a user
    public List<Expense> getExpensesByEmail(String email) {
        String sql = "SELECT * FROM expenses WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, new RowMapper<Expense>() {
            public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
                Expense e = new Expense();
                e.setId(rs.getInt("id"));
                e.setEmail(rs.getString("email"));
                e.setAmount(rs.getDouble("amount"));
                e.setCategory(rs.getString("category"));
                e.setWallet(rs.getString("wallet"));
                e.setDescription(rs.getString("description"));
                e.setDate(rs.getString("date"));
                return e;
            }
        });
    }

    // Wallet transfer: creates two transactions
    public void transfer(String email, String fromWallet, String toWallet, double amount, String description, String date) {
        // Deduct from fromWallet
        String sqlDeduct = "INSERT INTO expenses (email, amount, category, wallet, description, date) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlDeduct, email, -amount, "Transfer", fromWallet, description + " (deducted)", date);

        // Add to toWallet
        String sqlAdd = "INSERT INTO expenses (email, amount, category, wallet, description, date) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlAdd, email, amount, "Transfer", toWallet, description + " (added)", date);
    }
}
