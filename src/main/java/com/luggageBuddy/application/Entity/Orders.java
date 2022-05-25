package com.luggageBuddy.application.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private String locationId;
    private String cityName;
    private String customerId;
    private String customerName;
    private Integer noOfBags;
    private Date orderStart;
    private String status ="pending";
    private Date orderEnd;
    private Float currentAmount=0.0f;
    private Float finalAmount=0.0f;


}
