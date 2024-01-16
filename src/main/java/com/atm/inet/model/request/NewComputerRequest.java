package com.atm.inet.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewComputerRequest {

    private String name;
    private String code;
    private String processor;
    private String ram;
    private String monitor;
    private String ssd;
    private String vga;
    private String category;
    private Long price;


}
