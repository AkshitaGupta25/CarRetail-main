package com.example.carRetail.serviceImplementation;

import com.example.carRetail.entity.Buyer;
import com.example.carRetail.entity.Car;
import com.example.carRetail.entity.User;
import com.example.carRetail.repository.AdminRepository;
import com.example.carRetail.repository.BuyerRepository;
import com.example.carRetail.repository.CarRepository;
import com.example.carRetail.service.BuyerService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BuyerRepository buyerRepository;

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

    public ResponseEntity<Car> getCarDetail(Long id)
    {

        if(carRepository.findById(id).isPresent())
        {
            return new ResponseEntity(carRepository.findById(id), HttpStatus.OK);
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

    @Override
    public ResponseEntity<String> buyCar(Long carId, String token) {
        token = token.substring(7);
        String[] split = token.split("\\.");
        Base64.Decoder decoder=Base64.getUrlDecoder();
        String payload = new String(decoder.decode(split[1]));
        JSONObject jsonObject = new JSONObject(payload);
        String email = jsonObject.getString("sub");

        if (adminRepository.findByEmail(email) != null){
            User user = adminRepository.findByEmail(email);
            Car car = carRepository.findById(carId).get();
            Buyer buyer=new Buyer();
            buyer.setUser(user);
            buyer.setCar(car);
            buyerRepository.save(buyer);
            return new ResponseEntity<>("Request successfully sent",HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Unable to make a request to buy", HttpStatus.BAD_REQUEST);
        }
    }
}
