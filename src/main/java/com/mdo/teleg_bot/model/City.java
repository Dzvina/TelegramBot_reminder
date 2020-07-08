package com.mdo.teleg_bot.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "city")
public class City {

    private String cityName;
    private double longitude;
    private double latitude;
    private String country;

}
