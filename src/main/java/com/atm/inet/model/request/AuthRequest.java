package com.atm.inet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private @Nullable String firstName;
    private @Nullable String lastName;

    @NotBlank(message = "Email Can Not Be Empty")
    @Email
    private String email;

    private @Nullable String phoneNumber;

    @NotBlank(message = "Password Can Not Be Empty")
    private String password;
}
