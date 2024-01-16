package com.atm.inet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComputerSpecRequest {

    private String processor;
    private String ram;
    private String monitor;
    private String ssd;
    private String vga;

}
