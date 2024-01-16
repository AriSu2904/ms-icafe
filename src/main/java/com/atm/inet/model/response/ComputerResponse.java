package com.atm.inet.model.response;

import com.atm.inet.entity.computer.ComputerSpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComputerResponse {

    private String id;
    private String name;
    private String code;
    private String status;
    private TypeResponse type;
    private ComputerSpecResponse specification;

}
