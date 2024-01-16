package com.atm.inet.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NotBlank
@NoArgsConstructor
@AllArgsConstructor
public class ComputerRequest {

    private @Nullable String id;

    @NotBlank(message = "name can not be empty")
    private String name;
    @NotBlank(message = "pc_code can not be empty")
    private String code;
    @NotBlank(message = "category can not be empty")
    private String category;
    @NotBlank(message = "price can not be empty")
    private Long price;
    @NotBlank(message = "processor can not be empty")
    private String processor;
    @NotBlank(message = "ram can not be empty")
    private String ram;
    @NotBlank(message = "monitor can not be empty")
    private String monitor;
    @NotBlank(message = "ssd can not be empty")
    private String ssd;
    @NotBlank(message = "vga can not be empty")
    private String vga;
}
