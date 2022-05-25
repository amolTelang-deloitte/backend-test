package com.luggageBuddy.application.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    private String locationPhno;
    private String locationPassword;
    private String locationName;
    private String locationOwnersFirstName;
    private String locationOwnersLastName;
    private String locationEmail;
    private String address;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch=FetchType.LAZY)
    private City city;
    private Float latitude;
    private Float longitude;
    private String imageBase64;
    private Integer totalInventory=0;
    private Integer usedInventory=0;
    private Integer availableInventory=0;
    private String workingHours;
    private Boolean availability=false;
}
