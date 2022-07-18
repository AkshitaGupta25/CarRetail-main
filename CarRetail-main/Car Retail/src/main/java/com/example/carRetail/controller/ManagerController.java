package com.example.carRetail.controller;

import com.example.carRetail.entity.Car;
import com.example.carRetail.entity.User;
import com.example.carRetail.exceptions.CSVException;
import com.example.carRetail.model.CommonResponse;
import com.example.carRetail.model.UserDto;
import com.example.carRetail.serviceImplementation.CSVCarServiceImpl;
import com.example.carRetail.serviceImplementation.ManagerServiceImpl;
import com.example.carRetail.utility.CSVUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/car")
public class ManagerController {

    @Autowired
    private ManagerServiceImpl managerService;

    @Autowired
    private CSVCarServiceImpl csvCarService;

    @Value("${FILE_TYPE}")
    private String fileType;

    //for adding multiple cars
    @PostMapping("/cars")
    public ResponseEntity<CommonResponse> uploadCarDetailsFile(@RequestParam("file") MultipartFile file) {
        if (CSVUtility.hasCSVFormat(file, fileType)) {
            return csvCarService.saveCarDetails(file);
        }
        throw new CSVException("Please upload a csv file!");
    }

    //Download all car details in csv file
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() {
        return csvCarService.download();
    }

    //Onboard a single car
    @PostMapping
    public ResponseEntity<String> saveDetails(@RequestBody Car car)
    {
        return managerService.saveDetails(car);
    }

    //Get details for all cars
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars()
    {
        return managerService.getAllCars();
    }

    //Get details for a particular car
    @GetMapping("{id}")
    public ResponseEntity<Car> getCarDetail(@PathVariable Long id)
    {
        return managerService.getCarDetail(id);
    }

    //Update car details
    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Car car)
    {
        return managerService.update(id, car);
    }

    //Delete car
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id)
    {
        return managerService.deleteCar(id);
    }

    //Fetch all Buyers
    @GetMapping("/fetchBuyers")
    public ResponseEntity<List<UserDto>> getAllBuyers()
    {
        return managerService.getAllBuyers();
    }

    //Onboard buyers
    @PostMapping("/addBuyer")
    public ResponseEntity<String> addBuyer(@RequestBody User user)
    {
        return managerService.addBuyer(user);
    }

    //After 1st login Manager can update password
    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestParam String password, @RequestHeader(value="Authorization" )String token ){

        return managerService.updatePassword(password,token);
    }

}