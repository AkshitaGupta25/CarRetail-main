package com.example.carRetail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarDto {
    private String regNumber;
    private String rto;
    private String registrationState;
    private Integer registrationYear;
    private String make;
    private String model;
    private Integer mileage;
    private String bodyType;
    private Integer carScore;
    private String variant;
    private String chassisNumber;
    private String colour;
    private Integer yearOfManufacture;

}
