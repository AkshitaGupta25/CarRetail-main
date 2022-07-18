package com.example.carRetail.serviceImplementation;

import com.example.carRetail.entity.Car;
import com.example.carRetail.entity.User;
import com.example.carRetail.model.UserDto;
import com.example.carRetail.repository.AdminRepository;
import com.example.carRetail.repository.CarRepository;
import com.example.carRetail.service.ManagerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    EmailService emailService;

    @Autowired
    CarRepository carRepository;

    @Autowired
    AdminRepository adminRepository;
    public static boolean isValidPassword(String password)
    {

        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (password == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(password);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }
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

    public ResponseEntity<String> saveDetails(Car car)
    {
        if(carRepository.findByRegNumber(car.getRegNumber()) != null){
            return new ResponseEntity<>("Car already exist", HttpStatus.BAD_REQUEST);
        }
        else {
            carRepository.save(car);
            return new ResponseEntity<>("Car Onboarded Successfully",HttpStatus.OK);
        }
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
        {
            return new ResponseEntity("No car exist",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(carRepository.findAllByDeleteStatus(false),HttpStatus.OK);
    }

    public ResponseEntity<String> update(Long id , Car updatedCar) {
        if(carRepository.findById(id).isPresent()) {
            Car car = carRepository.findById(id).get();
            car.setRegNumber(updatedCar.getRegNumber());
            car.setCarScore(updatedCar.getCarScore());
            car.setColour(updatedCar.getColour());
            car.setChassisNumber(updatedCar.getChassisNumber());
            car.setMake(updatedCar.getMake());
            car.setRegistrationState(updatedCar.getRegistrationState());
            car.setMileage(updatedCar.getMileage());
            car.setRto(updatedCar.getRto());
            car.setRegistrationYear(updatedCar.getRegistrationYear());
            car.setYearOfManufacture(updatedCar.getYearOfManufacture());
            car.setBodyType(updatedCar.getBodyType());
            car.setDeleteStatus(updatedCar.isDeleteStatus());

            carRepository.save(car);
            return new ResponseEntity<>("Updated car details successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("Updation failed",HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> deleteCar(Long id){

        if(carRepository.findById(id).isPresent())
        {
            Car car = carRepository.findById(id).get();
            car.setDeleteStatus(true);
            carRepository.save(car);
            return new ResponseEntity<>("Deleted Car successfully",HttpStatus.OK);

        }
        return new ResponseEntity<>("Car doesn't exists",HttpStatus.OK);

    }

    public ResponseEntity<List<UserDto>> getAllBuyers()
    {
        List<User> userList = adminRepository.findAllByDeleteStatusAndRoleId(false,3L);
        List<UserDto> userDtoList = userList.stream().map(user -> UserDto.builder().email(user.getEmail()).address(user.getAddress()).firstName(user.getFirstName())
                .lastName(user.getLastName()).mobileNumber(user.getMobileNo()).role(user.getRole().getRoleName()).build()).collect(Collectors.toList());
        if (userList.isEmpty())
        {
            return new ResponseEntity("No buyer exist",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userDtoList,HttpStatus.OK);

    }

    public ResponseEntity<String> addBuyer(User user){
        if((user.getRole().getId())==3){
            String password = passwordGenerator();
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            adminRepository.save(user);
            emailService.sendMail(user.getEmail(),password);
            return new ResponseEntity<>("Buyer onboarded successfully ",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("You can add only buyer",HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<String> updatePassword(String password, String token) {
        token = token.substring(7);
        String[] split = token.split("\\.");
        Base64.Decoder decoder=Base64.getUrlDecoder();
        String payload = new String(decoder.decode(split[1]));
        JSONObject jsonObject = new JSONObject(payload);
        String email = jsonObject.getString("sub");

        if(adminRepository.findByEmail(email) != null){
            User user = adminRepository.findByEmail(email);
            if(isValidPassword(password))
            {
                user.setPassword(new BCryptPasswordEncoder().encode(password));
                adminRepository.save(user);
                return new ResponseEntity<>("Password Updated Successfully", HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("Password is not valid", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Unable to update password", HttpStatus.BAD_REQUEST);

    }
}
