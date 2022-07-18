package com.example.carRetail.service;

import com.example.carRetail.entity.Car;
import com.example.carRetail.entity.User;
import com.example.carRetail.model.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
public interface AdminService {

    ResponseEntity<String> addUser(User user);
    ResponseEntity<List<UserDto>> fetchAllUsers();
    ResponseEntity<String> updateUser(Long id, User updatedUser);
    ResponseEntity<String> deleteUser(Long id);
    ResponseEntity<Car> getCarDetail(Long id);
    ResponseEntity<List<Car>> getAllCars();
}
