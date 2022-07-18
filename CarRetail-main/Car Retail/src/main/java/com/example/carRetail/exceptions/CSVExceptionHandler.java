package com.example.carRetail.exceptions;

import com.example.carRetail.model.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;

@RestControllerAdvice
public class CSVExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CSVException.class)
    public ResponseEntity<CommonResponse> csvExceptionHandler(CSVException csvException) {
        String exceptionMessage = csvException.getMessage();
        CommonResponse response =
                CommonResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).data(new ArrayList<>()).message(exceptionMessage).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
