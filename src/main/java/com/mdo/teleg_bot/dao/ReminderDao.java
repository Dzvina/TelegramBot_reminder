package com.mdo.teleg_bot.dao;

import com.mdo.teleg_bot.model.Reminder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReminderDao {
    private final JdbcTemplate jdbcTemplate;

    public ReminderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addNewReminder(Reminder reminder) {
        String sql = "INSERT INTO REMINDER (user_id, message, date, time) value (?,?,?,?)";
        System.out.println(reminder);
        jdbcTemplate.update(sql, reminder.getUserId(), reminder.getMessage(), reminder.getDate(), reminder.getTime());
    }

    public List<Reminder> getAllRemindersByUserId(long userId){
        String sql = "SELECT * FROM reminder where user_id = ?";
        return jdbcTemplate.query(sql,new Object[]{userId}, new BeanPropertyRowMapper<>(Reminder.class));
    }

    public void deleteReminderById(long reminderId){
        String sql = "DELETE FROM reminder WHERE reminder_id = ?";
        jdbcTemplate.update(sql, reminderId);
    }

}
