package com.example.expensetracker.dao;

import com.example.expensetracker.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getUserIdByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, email);
        } catch (Exception e) {
            return null;
        }
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), email);
        return users.isEmpty() ? null : users.get(0);
    }

    public void registerUser(User user) {
    }
}
