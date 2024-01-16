package com.atm.inet.controller;

import com.atm.inet.model.common.CommonResponse;
import com.atm.inet.model.request.ComputerRequest;
import com.atm.inet.model.response.NewComputerResponse;
import com.atm.inet.service.ComputerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ComputerControllerTest {

    @Mock
    private ComputerService computerService;

    private ComputerController computerController;

    @BeforeEach
    void setUp() {
        computerService = mock(ComputerService.class);
        computerController = new ComputerController(new ObjectMapper(), computerService);
    }

    @Test
    @DisplayName("Test add computer")
    void testAddComputer() {
        ComputerRequest request = new ComputerRequest();
        MultipartFile multipartFile = mock(MultipartFile.class);
        NewComputerResponse newComputerResponse = new NewComputerResponse();
        when(computerService.save(request, multipartFile)).thenReturn(newComputerResponse);

        ResponseEntity<CommonResponse<NewComputerResponse>> responseEntity = computerController.addComputer(String.valueOf(request), multipartFile);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(newComputerResponse, responseEntity.getBody().getData());
    }

    @Test
    void getAll() {
    }

    @Test
    void getById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateComputer() {
    }
}