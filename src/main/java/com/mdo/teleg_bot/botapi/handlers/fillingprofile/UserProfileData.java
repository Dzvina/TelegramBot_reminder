package com.mdo.teleg_bot.botapi.handlers.fillingprofile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {

    String location;
    LocalDate date;
    LocalTime time;
    String message;
}
