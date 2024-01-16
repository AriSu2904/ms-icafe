package com.atm.inet.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewComputerResponse {

    String id;
    String name;
    String code;
    String status;
    TypeResponse category;
    ComputerSpecResponse computerSpec;

}
