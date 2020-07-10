package com.mdo.teleg_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
//@Builder
@Entity
@Table(name = "city")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reminder {

    long reminderId;
    long userId;
    String message;
    LocalDate date;
    LocalTime time;
}
