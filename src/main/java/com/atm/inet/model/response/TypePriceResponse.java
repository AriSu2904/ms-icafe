package com.atm.inet.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypePriceResponse {

    private String id;
    private Long price;
    private Boolean isActive;

}
