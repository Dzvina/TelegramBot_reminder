package com.mdo.teleg_bot.dao;

import com.mdo.teleg_bot.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Message;

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
}
