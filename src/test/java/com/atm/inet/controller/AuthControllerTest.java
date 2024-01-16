package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.request.AuthRequest;
import com.atm.inet.model.response.LoginResponse;
import com.atm.inet.model.response.RegisterResponse;
import com.atm.inet.service.AdminService;
import com.atm.inet.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Test
    @DisplayName("Test register customer")
    void testRegisterCustomer() {
        AuthRequest authRequest = new AuthRequest();
        RegisterResponse registerResponse = new RegisterResponse();
        when(authService.registerCustomer(authRequest)).thenReturn(registerResponse);

        ResponseEntity<CommonResponse<RegisterResponse>> responseEntity = authController.registerCustomer(authRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(registerResponse, responseEntity.getBody().getData());
    }

    @Test
    @DisplayName("test Register Admin")
    void testRegisterAdmin() {
        AuthRequest authRequest = new AuthRequest();
        RegisterResponse registerResponse = new RegisterResponse();
        when(authService.registerAdmin(authRequest)).thenReturn(registerResponse);

        ResponseEntity<CommonResponse<RegisterResponse>> responseEntity = authController.registerAdmin(authRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(registerResponse, responseEntity.getBody().getData());
    }

    @Test
    @DisplayName("test login user")
    void testLogin() {
        AuthRequest authRequest = new AuthRequest();
        LoginResponse loginResponse = new LoginResponse();
        when(authService.login(authRequest)).thenReturn(loginResponse);

        ResponseEntity<CommonResponse<LoginResponse>> responseEntity = authController.login(authRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(loginResponse, responseEntity.getBody().getData());
    }
}