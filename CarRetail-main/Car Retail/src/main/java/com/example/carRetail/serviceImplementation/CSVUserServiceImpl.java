package com.example.carRetail.serviceImplementation;

import com.example.carRetail.entity.User;
import com.example.carRetail.exceptions.CSVException;
import com.example.carRetail.model.CommonResponse;
import com.example.carRetail.model.UserDto;
import com.example.carRetail.repository.AdminRepository;
import com.example.carRetail.repository.RoleRepository;
import com.example.carRetail.service.CSVUserService;
import com.example.carRetail.utility.CSVUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CSVUserServiceImpl implements CSVUserService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<CommonResponse> saveUserDetails(MultipartFile file) {
        List<UserDto> usersDto = CSVUtility.csvToUserDto(file);

        try {
            List<User> users =
                    adminRepository.saveAll(usersDto.stream().filter(userDto ->
                                    !("ROLE_ADMIN".equals(userDto.getRole())) && !adminRepository.existsByEmail(userDto.getEmail()))
                            .map(userDto -> {
                                User user = User.builder().firstName(userDto.getFirstName()).lastName(userDto.getLastName()).address(userDto.getAddress()).email(userDto.getEmail()).mobileNo(userDto.getMobileNumber()).build();
                                roleRepository.findByRoleName(userDto.getRole()).ifPresentOrElse(user::setRole, () -> {
                                    throw new CSVException("Invalid file : User Role doesn't exist for user name " + userDto.getFirstName() +
                                            " with email" +
                                            " :" + userDto.getEmail());
                                });
                                return user;


                            }).collect(Collectors.toList()));
            return new ResponseEntity<>(CommonResponse.builder().message("Records inserted : " + users.size()).statusCode(HttpStatus.OK.value()).data(users).build(), HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {
            throw new CSVException("Could not insert the records due to duplicate emails");
        }

    }
}
