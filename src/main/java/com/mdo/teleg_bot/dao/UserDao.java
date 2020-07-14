package com.mdo.teleg_bot.dao;

import com.mdo.teleg_bot.model.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUserLocation(User user) {
        String sql = "INSERT INTO USER (user_id, user_location) value (?,?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getUserLocation());
    }

    public User getUserByUserId(long userId) {
        String sql = "SELECT * FROM user where user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            return null;
        }
    }

    public void updateUserByUserId(long userId, String userLocation) {
        String sql = "UPDATE user SET user_location = ? where user_id = ?";
       jdbcTemplate.update(sql, userLocation, userId);
    }
}
