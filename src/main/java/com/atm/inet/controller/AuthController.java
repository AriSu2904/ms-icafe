package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.request.AuthRequest;
import com.atm.inet.model.response.LoginResponse;
import com.atm.inet.model.response.RegisterResponse;
import com.atm.inet.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "/register")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerCustomer(@RequestBody AuthRequest request){
        RegisterResponse registerResponse = authService.registerCustomer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body((CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully Create Customer!")
                .data(registerResponse)
                .build()));
    }

    @PostMapping(path = "/register-admin")
    public ResponseEntity<CommonResponse<RegisterResponse>> registerAdmin(@RequestBody AuthRequest request){
        RegisterResponse registerResponse = authService.registerAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponse.<RegisterResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully Create Admin!")
                        .data(registerResponse)
                .build());
    }

    @PostMapping(path = "/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody AuthRequest request){
        LoginResponse loggedAdmin = authService.login(request);
        return ResponseEntity.ok(CommonResponse.<LoginResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully Login!")
                        .data(loggedAdmin)
                .build());
    }

}
