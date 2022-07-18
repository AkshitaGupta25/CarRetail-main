package com.example.carRetail.controller;

import com.example.carRetail.entity.Car;
import com.example.carRetail.entity.User;
import com.example.carRetail.exceptions.CSVException;
import com.example.carRetail.model.CommonResponse;
import com.example.carRetail.model.UserDto;
import com.example.carRetail.serviceImplementation.AdminServiceImpl;
import com.example.carRetail.serviceImplementation.CSVUserServiceImpl;
import com.example.carRetail.utility.CSVUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminServiceImpl;

    @Autowired
    private CSVUserServiceImpl csvUserService;

    @Value("${FILE_TYPE}")
    private String fileType;

    //for Adding a single user
    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody User user)
    {
        return adminServiceImpl.addUser(user);
    }

    //add multiple users
    @PostMapping("/users")
    public ResponseEntity<CommonResponse> uploadUserDetailsFile(@RequestParam("file") MultipartFile file) {

        if (CSVUtility.hasCSVFormat(file, fileType)) {
            return csvUserService.saveUserDetails(file);
        }
        throw new CSVException("Please upload a csv file!");
    }

    //Fetch all users
    @GetMapping
    public ResponseEntity<List<UserDto>> fetchAllUsers()
    {
        return adminServiceImpl.fetchAllUsers();
    }

    //Update user
    @PutMapping("{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user)
    {
        return adminServiceImpl.updateUser(id,user);
    }

    //Delete user
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id)
    {
        return adminServiceImpl.deleteUser(id);
    }

    //Get details for a particular car
    @GetMapping("/fetchCar/{id}")
    public ResponseEntity<Car> getCarDetail(@PathVariable Long id)
    {
        return adminServiceImpl.getCarDetail(id);
    }

    //Get details for all cars
    @GetMapping("/fetchCars")
    public ResponseEntity<List<Car>> getAllCars()
    {
        return adminServiceImpl.getAllCars();
    }
}