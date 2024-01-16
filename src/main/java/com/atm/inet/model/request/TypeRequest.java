package com.atm.inet.model.request;

import com.atm.inet.model.response.FileResponse;
import com.atm.inet.model.response.TypePriceResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeRequest {

    private String id;
    private Long price;

}
