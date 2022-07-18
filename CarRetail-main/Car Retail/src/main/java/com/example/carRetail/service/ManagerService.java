package com.example.carRetail.service;

import com.example.carRetail.entity.Car;
import com.example.carRetail.entity.User;
import com.example.carRetail.model.UserDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface ManagerService {

     ResponseEntity<String> saveDetails(Car car);
     ResponseEntity<Car> getCarDetail(Long id);
     ResponseEntity<List<Car>> getAllCars();
     ResponseEntity<String> update(Long id , Car car);
     ResponseEntity<String> deleteCar(Long id);
     ResponseEntity<List<UserDto>> getAllBuyers();
     ResponseEntity<String> addBuyer(User user);
     ResponseEntity<String> updatePassword(String password, String token);
}
