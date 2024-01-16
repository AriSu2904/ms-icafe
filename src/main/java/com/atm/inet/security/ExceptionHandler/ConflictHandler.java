package com.atm.inet.security.ExceptionHandler;


import com.atm.inet.model.common.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@ControllerAdvice
@RequiredArgsConstructor
public class ConflictHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    public void handleConflictException(HttpServletRequest request, HttpServletResponse response, HttpClientErrorException.Conflict ex)
            throws IOException {
        CommonResponse<Object> commonResponse = CommonResponse.builder()
                .statusCode(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();

        String result = objectMapper.writeValueAsString(commonResponse);


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        response.getWriter().write(result);
    }


}
