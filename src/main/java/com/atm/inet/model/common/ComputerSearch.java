package com.atm.inet.model.common;

import com.atm.inet.entity.constant.ECategory;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class ComputerSearch {

    private String name;
    private String code;
    private String processor;
    private String vga;
    private String category;
    private String status;

}
