package com.example.expensetracker.dao;

import com.example.expensetracker.model.Income;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IncomeDAO {

    private final JdbcTemplate jdbcTemplate;

    public IncomeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ Insert income into MySQL
    public void save(Income income) {
        String sql = "INSERT INTO income (user_id, amount, source, date, wallet) VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                income.getAmount(),
                income.getWallet(),
                income.getSource(),
                income.getDate());
    }

    // ✅ Retrieve all incomes
    public List<Income> findAll() {
        String sql = "SELECT * FROM income ORDER BY id DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Income(
                        rs.getInt("id"),
                        rs.getDouble("amount"),
                        rs.getString("wallet"),
                        rs.getString("source"),
                        rs.getString("date")
                )
        );
    }

}
