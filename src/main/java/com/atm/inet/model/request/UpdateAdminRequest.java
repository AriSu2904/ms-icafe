package com.atm.inet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAdminRequest {
    @NotBlank(message = "admin id cannot be empty")
    private String adminId;
    private String fullName;
    private String phoneNumber;
}

