package com.example.expensetracker.dao;

import com.example.expensetracker.model.Expense;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExpenseMapper implements RowMapper<Expense> {
    @Override
    public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
        Expense expense = new Expense();
        expense.setId(rs.getInt("id"));
        expense.setUserId(rs.getInt("user_id"));
        expense.setCategory(rs.getString("category"));
        expense.setAmount(rs.getDouble("amount"));
        expense.setDescription(rs.getString("description"));
        expense.setDate(rs.getObject("date", LocalDate.class));
        return expense;
    }
}
