package com.mdo.teleg_bot.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "city")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class City {

    String cityName;
    double longitude;
    double latitude;
    String country;

}
