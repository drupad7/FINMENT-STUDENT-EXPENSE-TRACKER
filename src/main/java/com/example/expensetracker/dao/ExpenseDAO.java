package com.example.expensetracker.dao;

import com.example.expensetracker.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExpenseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Expense expense) {
        String sql = "INSERT INTO expenses (user_id, category, amount, description, date, wallet) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, expense.getUserId(), expense.getCategory(),
                expense.getAmount(), expense.getDescription(), expense.getDate());
    }

    public List<Expense> findByUserId(int userId) {
        String sql = "SELECT * FROM expenses WHERE user_id = ?";
        return jdbcTemplate.query(sql, new ExpenseMapper(), userId);
    }

    // NEW: find expenses by user's email (join with users table)
    public List<Expense> findByUserEmail(String email) {
        String sql = "SELECT e.* FROM expenses e JOIN users u ON e.user_id = u.id WHERE u.email = ?";
        return jdbcTemplate.query(sql, new ExpenseMapper(), email);
    }

    public List<Expense> findAll() {
        String sql = "SELECT * FROM expenses";
        return jdbcTemplate.query(sql, new ExpenseMapper());
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
