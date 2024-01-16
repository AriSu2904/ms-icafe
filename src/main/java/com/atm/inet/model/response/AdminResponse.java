package com.atm.inet.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    private String adminId;
    private String email;
    private String fullName;
    private String phoneNumber;
}
