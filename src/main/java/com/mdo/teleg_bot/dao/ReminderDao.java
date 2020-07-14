package com.mdo.teleg_bot.dao;

import com.mdo.teleg_bot.model.Reminder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReminderDao {
    private final JdbcTemplate jdbcTemplate;

    public ReminderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addNewReminder(Reminder reminder){
        String sql = "INSERT INTO REMINDER (user_id, message, date, time) value (?,?,?,?)";
        jdbcTemplate.update(sql, reminder.getUserId(), reminder.getMessage(), reminder.getDate(), reminder.getTime());
    }



}
