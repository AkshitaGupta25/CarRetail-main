package com.example.carRetail.service;


import com.example.carRetail.model.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CSVUserService {
    ResponseEntity<CommonResponse> saveUserDetails(MultipartFile file);
}
