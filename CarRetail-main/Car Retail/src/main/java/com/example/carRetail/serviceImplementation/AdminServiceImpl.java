package com.example.carRetail.serviceImplementation;

import com.example.carRetail.entity.Car;
import com.example.carRetail.entity.User;
import com.example.carRetail.model.UserDto;
import com.example.carRetail.repository.AdminRepository;
import com.example.carRetail.repository.CarRepository;
import com.example.carRetail.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CarRepository carRepository;

    public String passwordGenerator(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedPassword = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedPassword;
    }
    public ResponseEntity<String> addUser(User user){
        if(!((user.getRole().getId()).equals(1))) {
            if (adminRepository.findByEmail(user.getEmail()) != null) {
                return new ResponseEntity<>("User already registered!", HttpStatus.BAD_REQUEST);
            } else {
                String password = passwordGenerator();
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                adminRepository.save(user);
                emailService.sendMail(user.getEmail(), password);
                return new ResponseEntity<>("User account created successfully!", HttpStatus.OK);
            }
        }
        else {
            return new ResponseEntity<>("Only managers and buyers could be added", HttpStatus.OK);

        }
    }


    public ResponseEntity<List<UserDto>> fetchAllUsers(){
        List<User> userList = adminRepository.findAllByDeleteStatus(false);
        List<UserDto> userDtoList = userList.stream().map(user -> UserDto.builder().email(user.getEmail()).address(user.getAddress()).firstName(user.getFirstName())
                .lastName(user.getLastName()).mobileNumber(user.getMobileNo()).role(user.getRole().getRoleName()).build()).collect(Collectors.toList());
        if(userList.isEmpty())
        {
            return new ResponseEntity("No User exist",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDtoList,HttpStatus.OK);
    }

    public ResponseEntity<String> updateUser(Long id, User updatedUser){
        if(adminRepository.findById(id).isPresent()) {
            User user = adminRepository.findById(id).get();
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setMobileNo(updatedUser.getMobileNo());
            user.setAddress(updatedUser.getAddress());
            user.setRole(updatedUser.getRole());
            user.setDeleteStatus(updatedUser.isDeleteStatus());
            user.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
            adminRepository.save(user);
            return new ResponseEntity<>("Updated user successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Given user doesn't exist",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteUser(Long id){

        if(adminRepository.findById(id).isPresent())
        {
            User user=adminRepository.findById(id).get();
            user.setDeleteStatus(true);
            adminRepository.save(user);
            return new ResponseEntity<>("Deleted user successfully",HttpStatus.OK);

        }
        return new ResponseEntity<>("User doesn't exist",HttpStatus.BAD_REQUEST);
    }



    public ResponseEntity<Car> getCarDetail(Long id)
    {
        if(carRepository.findById(id).isPresent())
        {
            return new ResponseEntity(carRepository.findById(id),HttpStatus.OK);
        }
        return new ResponseEntity("Car with given id doesn't exist",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Car>> getAllCars(){

        if(carRepository.findAllByDeleteStatus(false).isEmpty())
            return new ResponseEntity("No car exist",HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(carRepository.findAllByDeleteStatus(false),HttpStatus.OK);
    }
}
