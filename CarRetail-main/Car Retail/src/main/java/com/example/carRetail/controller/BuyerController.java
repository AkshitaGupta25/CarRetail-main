package com.example.carRetail.controller;

import com.example.carRetail.entity.Car;
import com.example.carRetail.model.JwtResponse;
import com.example.carRetail.serviceImplementation.BuyerServiceImpl;
import com.example.carRetail.serviceImplementation.CSVCarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/carDetails")
public class BuyerController {

    @Autowired
    JwtResponse jwtResponse;

    @Autowired
    BuyerServiceImpl buyerService;

    @Autowired
    private CSVCarServiceImpl csvCarService;


    //Get details for a particular car
    @GetMapping("{id}")
    public ResponseEntity<Car> getCarDetail(@PathVariable Long id)
    {
        return buyerService.getCarDetail(id);
    }

    //Get details of all car
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars()
    {
        System.out.println(jwtResponse.getJwtToken());
        return buyerService.getAllCars();
    }

    //After 1st login buyer can update password
    @PutMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestParam String password, @RequestHeader(value="Authorization")String token ){

        return buyerService.updatePassword(password,token);
    }

    //Download all car details in csv file
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() {
        return csvCarService.download();
    }

    //Request to buy car
    @PostMapping("/buy")
    public ResponseEntity<String> buyCar(@RequestParam Long id, @RequestHeader(value="Authorization" ) String token){
        return buyerService.buyCar(id,token);
    }
}
