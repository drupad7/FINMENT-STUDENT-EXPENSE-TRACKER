package com.example.expensetracker.dao;

import com.example.expensetracker.model.Income;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class IncomeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Add Income
    public int addIncome(int userId, double amount, String source, String date, String paymentMethod) {
        String sql = "INSERT INTO income (user_id, amount, source, date, payment_method) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, userId, amount, source, date, paymentMethod);
    }

    // Get total income for a user
    public double getTotalIncome(int userId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM income WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, userId);
    }

    // Get all income records for a user
    public List<Income> getAllIncomes(int userId) {
        String sql = "SELECT * FROM income WHERE user_id = ?";
        return jdbcTemplate.query(sql, new RowMapper<Income>() {
            @Override
            public Income mapRow(ResultSet rs, int rowNum) throws SQLException {
                Income income = new Income();
                income.setId(rs.getInt("id"));
                income.setUserId(rs.getInt("user_id"));
                income.setAmount(rs.getDouble("amount"));
                income.setSource(rs.getString("source"));
                income.setDate(rs.getString("date"));
                income.setPaymentMethod(rs.getString("payment_method"));
                return income;
            }
        }, userId);
    }
}
