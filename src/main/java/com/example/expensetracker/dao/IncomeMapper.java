package com.example.expensetracker.dao;

import com.example.expensetracker.model.Income;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncomeMapper implements RowMapper<Income> {
    @Override
    public Income mapRow(ResultSet rs, int rowNum) throws SQLException {
        Income income = new Income();
        income.setId(rs.getInt("id"));
        income.setUserId(rs.getInt("user_id"));
        income.setSource(rs.getString("source"));
        income.setAmount(rs.getDouble("amount"));
        income.setDate(rs.getString("date"));
        return income;
    }
}
