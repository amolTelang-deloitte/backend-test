package com.luggageBuddy.application.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    private String cityName;
    private Float pricePerHour;
    private Float pricePerDay;
}
