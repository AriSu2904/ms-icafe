package com.atm.inet.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeResponse {
    private String id;
    private String category;
    private List<TypePriceResponse> prices;
    private FileResponse image;

}
