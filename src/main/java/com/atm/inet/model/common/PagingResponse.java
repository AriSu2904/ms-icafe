package com.atm.inet.model.common;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse {
    private Long count;
    private Integer totalPages;
    private Integer page;
    private Integer size;
}
