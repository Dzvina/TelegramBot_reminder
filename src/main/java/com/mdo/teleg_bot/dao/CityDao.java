package com.mdo.teleg_bot.dao;

import com.mdo.teleg_bot.model.City;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Location;

@Repository
public class CityDao {
    final
    JdbcTemplate jdbcTemplate;

    public CityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public City getCityByCityName(String cityName) {
        String sql = "select * from city where city_name=?";
        City city = jdbcTemplate.queryForObject(sql, new Object[]{cityName}, new BeanPropertyRowMapper<>(City.class));
        return city;
    }
}
