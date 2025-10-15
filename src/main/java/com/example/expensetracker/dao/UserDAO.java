package com.example.expensetracker.dao;

import com.example.expensetracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ Add this method to fix your error
    public Integer getUserIdByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, email);
        } catch (Exception e) {
            return null; // if no user found
        }
    }

    // Optional: you can also add register & find methods
    public void registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword());
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                return u;
            }, email);
        } catch (Exception e) {
            return null;
        }
    }
}
