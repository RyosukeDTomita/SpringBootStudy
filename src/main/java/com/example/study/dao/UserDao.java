package com.example.study.dao;

import com.example.study.domain.User;
import com.example.study.domain.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        String sql = "SELECT user_id, profile FROM users WHERE user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new User(rs.getString("user_id"), rs.getString("profile")),
                userId
        ).stream().findFirst();
    }
}
