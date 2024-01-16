package com.atm.inet.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComputerSpecResponse {

    private String id;
    private String processor;
    private String ram;
    private String monitor;
    private String ssd;
    private String vga;



}
