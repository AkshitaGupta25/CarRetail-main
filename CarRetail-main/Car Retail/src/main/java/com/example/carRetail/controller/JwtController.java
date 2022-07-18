package com.example.carRetail.controller;

import com.example.carRetail.model.JwtRequest;
import com.example.carRetail.model.JwtResponse;
import com.example.carRetail.serviceImplementation.CustomUserDetailService;
import com.example.carRetail.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class JwtController {
    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
        }
        catch (UsernameNotFoundException e){
            System.out.println("UsernameNotFoundException");
        }
        catch (BadCredentialsException e){
            System.out.println("BadCredentialsException");
        }
        final UserDetails userDetails = this.userService.loadUserByUsername(jwtRequest.getEmail());
        final String token = this.jwtUtility.generateToken(userDetails);
        System.out.println("Token: " + token);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization","Bearer "+token);
        JwtResponse jwtResponse= new JwtResponse(token);
        return new ResponseEntity<>("Login successful",httpHeaders, HttpStatus.OK);
    }
}
