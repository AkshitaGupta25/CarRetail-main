package com.example.carRetail.service;

import com.example.carRetail.entity.Car;
import com.example.carRetail.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BuyerService {

    ResponseEntity<Car> getCarDetail(Long id);
    ResponseEntity<List<Car>> getAllCars();
    ResponseEntity<?> updatePassword(String password, String token);
    ResponseEntity<String> buyCar(Long id, String token);
}
