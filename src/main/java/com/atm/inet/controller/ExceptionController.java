package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusExceptionHandler(ResponseStatusException exception){
        CommonResponse<String> commonResponse =
                CommonResponse.<String>builder()
                        .statusCode(exception.getStatus().value())
                        .message(exception.getMessage())
                        .build();
        return ResponseEntity.status(exception.getStatus()).body(commonResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> responseStatusBadRequest(ConstraintViolationException exception) {
        CommonResponse<String> commonResponse = CommonResponse.<String>builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed: " + exception.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
    }

}
