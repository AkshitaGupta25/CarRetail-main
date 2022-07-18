package com.example.carRetail.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String regNumber;
    private String rto;
    private String registrationState;
    private Integer registrationYear;
    private Integer mileage;
    private String bodyType;
    private Integer carScore;
    private String variant;
    private String chassisNumber;
    private String colour;
    private Integer yearOfManufacture;
    private String make;
    private String model;
    @Column(columnDefinition = "boolean default false")
    private boolean deleteStatus;
}
