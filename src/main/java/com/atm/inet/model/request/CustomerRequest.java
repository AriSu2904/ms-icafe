package com.atm.inet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequest {

    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private @Nullable Boolean isMember;
    private @Nullable Boolean isDeleted;

}
